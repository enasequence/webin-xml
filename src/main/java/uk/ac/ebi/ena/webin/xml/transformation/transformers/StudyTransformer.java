/*
 * Copyright 2023 EMBL - European Bioinformatics Institute
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package uk.ac.ebi.ena.webin.xml.transformation.transformers;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import uk.ac.ebi.ena.sra.xml.AttributeType;
import uk.ac.ebi.ena.sra.xml.STUDYSETDocument;
import uk.ac.ebi.ena.sra.xml.StudyType;
import uk.ac.ebi.ena.sra.xml.XRefType;
import uk.ac.ebi.ena.webin.xml.transformation.WebinXmlTransformationException;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.dtos.StudyTransformationDTO;

public class StudyTransformer extends AbstractTransformer
    implements Transformer<StudyTransformationDTO, STUDYSETDocument> {

  public StudyTransformer(Templates transformationTemplate) {
    super(transformationTemplate);
  }

  @Override
  public Document transform(Document document) throws WebinXmlTransformationException {
    try {
      document = applyTemplateTransformation(document);
      return document;
    } catch (TransformerException e) {
      throw new WebinXmlTransformationException("Error applying study transformation.", e);
    } catch (Exception e) {
      throw new WebinXmlTransformationException(
          "Unexpected error while transforming study xml.", e);
    }
  }

  @Override
  public STUDYSETDocument transformForPresentation(
      StudyTransformationDTO studyDto, STUDYSETDocument studySetDocument)
      throws WebinXmlTransformationException {

    StudyType studyType = studySetDocument.getSTUDYSET().getSTUDYArray()[0];

    transformCommon(studyDto, studyDto.getStudyId(), studyType);

    transformIdentifiers(studyDto, studyType);

    // EMD-4253
    if (studyDto.getStudyId().startsWith("ERP")
        && null != studyType.getDESCRIPTOR().getRELATEDSTUDIES())
      studyType.getDESCRIPTOR().unsetRELATEDSTUDIES();

    transformLinks(studyDto, studyType);

    transformAttributes(studyDto, studyType);

    return studySetDocument;
  }

  private void transformIdentifiers(StudyTransformationDTO studyDto, StudyType studyType) {

    unsetIdentifiersSubmitterIdIfBlankAlias(studyType);

    // Injecting SECONDARY_ID
    if (null != studyDto.getProjectId())
      injectSecondaries(
          studyType.getIDENTIFIERS(),
          Stream.of(studyDto.getProjectId()).collect(Collectors.toSet()));

    if (studyDto.getStudyId().startsWith("ERP"))
      injectSecondaries(studyType.getIDENTIFIERS(), studyDto.getSecondary());

    fixIdentifiers(studyType);
  }

  private void transformLinks(StudyTransformationDTO studyDto, StudyType studyType) {

    retainOnlyPubmedAndURLLinks(
        studyType.getSTUDYLINKS(),
        studyType.getSTUDYLINKS() != null ? studyType.getSTUDYLINKS().getSTUDYLINKArray() : null);

    // inserting submission referenced in the study
    appendStudyLink(studyType, "ENA-SUBMISSION", studyDto.getSubmissionId());

    appendArrayExpressLink(studyType.getAlias(), () -> createNewLinkXRef(studyType));

    // FASTQ files
    appendStudyLink(
        studyType,
        ENA_FASTQ_FILES_TAG,
        String.format(ENA_FASTQ_FILES_URL_PREFORMAT, studyDto.getStudyId()),
        true);

    // Submitted files
    appendStudyLink(
        studyType,
        ENA_SUBMITTED_FILES_TAG,
        String.format(ENA_SUBMITTED_FILES_URL_PREFORMAT, studyDto.getStudyId()),
        true);
  }

  private void transformAttributes(StudyTransformationDTO studyDto, StudyType studyType) {
    addFirstPublicLastUpdateAttributes(studyDto, () -> createNewAttribute(studyType));

    // ArrayExpress Accession
    if (studyDto.getArrayExpressId() != null)
      appendAttribute(
          () -> createNewAttribute(studyType), "ArrayExpress", studyDto.getArrayExpressId());

    // EMD-2300 : converting pubmed attribute to XREF link
    migratePubMedAttrs(studyType);
  }

  private void appendStudyLink(StudyType studyType, String db, String id) {
    appendStudyLink(studyType, db, id, false);
  }

  private void appendStudyLink(StudyType studyType, String db, String id, boolean cdata) {
    appendLink(createNewLinkXRef(studyType), db, id, cdata);
  }

  private XRefType createNewLinkXRef(StudyType studyType) {
    StudyType.STUDYLINKS links = studyType.getSTUDYLINKS();
    links = links == null ? studyType.addNewSTUDYLINKS() : links;

    return links.addNewSTUDYLINK().addNewXREFLINK();
  }

  private AttributeType createNewAttribute(StudyType studyType) {
    StudyType.STUDYATTRIBUTES attributes = studyType.getSTUDYATTRIBUTES();
    if (attributes == null) {
      attributes = studyType.addNewSTUDYATTRIBUTES();
    }

    return attributes.addNewSTUDYATTRIBUTE();
  }

  private void migratePubMedAttrs(StudyType studyType) {
    if (null != studyType.getSTUDYATTRIBUTES()) {
      for (AttributeType studyAttribute : studyType.getSTUDYATTRIBUTES().getSTUDYATTRIBUTEArray()) {
        // PUBMED
        if (null != studyAttribute.getTAG()
            && studyAttribute.getTAG().matches("(?i)^PUBMED(_?ID)?$")) {
          if (null != studyAttribute.getVALUE()) {
            if (null == studyType.getSTUDYLINKS()) studyType.addNewSTUDYLINKS();

            XRefType xreflink = studyType.getSTUDYLINKS().addNewSTUDYLINK().addNewXREFLINK();
            xreflink.setDB("PUBMED");
            xreflink.setID(studyAttribute.getVALUE());
            Node nodeStudyAttr = studyAttribute.getDomNode();
            studyType.getSTUDYATTRIBUTES().getDomNode().removeChild(nodeStudyAttr);
            if (null != studyType.getSTUDYATTRIBUTES()
                && 0 == studyType.getSTUDYATTRIBUTES().sizeOfSTUDYATTRIBUTEArray())
              studyType.unsetSTUDYATTRIBUTES();
          }
        }
      }
    }
  }
}
