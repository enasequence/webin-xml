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

import java.util.Map;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import uk.ac.ebi.ena.sra.xml.ANALYSISSETDocument;
import uk.ac.ebi.ena.sra.xml.AnalysisType;
import uk.ac.ebi.ena.sra.xml.AttributeType;
import uk.ac.ebi.ena.sra.xml.IdentifierType;
import uk.ac.ebi.ena.sra.xml.QualifiedNameType;
import uk.ac.ebi.ena.sra.xml.XRefType;
import uk.ac.ebi.ena.webin.xml.transformation.WebinXmlTransformationException;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.dtos.AnalysisTransformationDTO;

public class AnalysisTransformer extends AbstractTransformer
    implements Transformer<AnalysisTransformationDTO, ANALYSISSETDocument> {

  public AnalysisTransformer(Templates transformationTemplate) {
    super(transformationTemplate);
  }

  @Override
  public Document transform(Document document) throws WebinXmlTransformationException {
    try {
      document = applyTemplateTransformation(document);

      return document;
    } catch (TransformerException e) {
      throw new WebinXmlTransformationException("Error applying analysis transformation.", e);
    } catch (Exception e) {
      throw new WebinXmlTransformationException(
          "Unexpected error while transforming analysis xml.", e);
    }
  }

  @Override
  public ANALYSISSETDocument transformForPresentation(
      AnalysisTransformationDTO analysisDto, ANALYSISSETDocument analysisSetDocument)
      throws WebinXmlTransformationException {

    AnalysisType analysisType = analysisSetDocument.getANALYSISSET().getANALYSISArray()[0];

    transformCommon(analysisDto, analysisDto.getAnalysisId(), analysisType);

    analysisType.setAccession(analysisDto.getAnalysisId());

    if (null != analysisType.getSTUDYREF() && null != analysisDto.getStudyId()) {
      if (null == analysisType.getSTUDYREF().getAccession()
          || analysisType.getSTUDYREF().getAccession().isEmpty())
        analysisType.getSTUDYREF().setAccession(analysisDto.getStudyId());
    }

    transformIdentifiers(analysisDto, analysisType);

    if (analysisDto.getAnalysisId().startsWith("ERZ"))
      expandSamples(analysisType, analysisDto.getSampleAccessions());

    transformLinks(analysisDto, analysisType);

    transformAttributes(analysisDto, analysisType);

    return analysisSetDocument;
  }

  private void transformIdentifiers(
      AnalysisTransformationDTO analysisDto, AnalysisType analysisType) {

    if (analysisDto.getAnalysisId().startsWith("ERZ"))
      injectSecondaries(analysisType.getIDENTIFIERS(), analysisDto.getSecondary());

    unsetIdentifiersSubmitterIdIfBlankAlias(analysisType);

    fixIdentifiers(analysisType);
  }

  private void expandSamples(AnalysisType experimentType, Map<String, String> sample_list) {
    for (AnalysisType.SAMPLEREF sampleref : experimentType.getSAMPLEREFArray()) {
      IdentifierType identifiers1 = sampleref.getIDENTIFIERS();

      if (identifiers1 != null) {
        String primary_id = identifiers1.getPRIMARYID().getStringValue();

        if (sample_list.containsKey(primary_id)) {
          QualifiedNameType qualifiedNameType = sampleref.getIDENTIFIERS().addNewEXTERNALID();

          qualifiedNameType.setNamespace("BioSample");
          qualifiedNameType.setStringValue(sample_list.get(primary_id));
        }
      }
    }
  }

  private void transformLinks(AnalysisTransformationDTO analysisDto, AnalysisType analysisType) {
    appendAnalysisLink(analysisType, "ENA-SUBMISSION", analysisDto.getSubmissionId());
  }

  private void transformAttributes(
      AnalysisTransformationDTO analysisDto, AnalysisType analysisType) {

    addFirstPublicLastUpdateAttributes(analysisDto, () -> createNewAttribute(analysisType));
  }

  private void appendAnalysisLink(AnalysisType analysisType, String db, String id) {
    appendAnalysisLink(analysisType, db, id, false);
  }

  private void appendAnalysisLink(AnalysisType analysisType, String db, String id, boolean cdata) {
    appendLink(createNewLinkXRef(analysisType), db, id, cdata);
  }

  private XRefType createNewLinkXRef(AnalysisType analysisType) {
    AnalysisType.ANALYSISLINKS links = analysisType.getANALYSISLINKS();
    links = links == null ? analysisType.addNewANALYSISLINKS() : links;

    return links.addNewANALYSISLINK().addNewXREFLINK();
  }

  private AttributeType createNewAttribute(AnalysisType analysisType) {
    AnalysisType.ANALYSISATTRIBUTES attributes = analysisType.getANALYSISATTRIBUTES();
    if (attributes == null) {
      attributes = analysisType.addNewANALYSISATTRIBUTES();
    }

    return attributes.addNewANALYSISATTRIBUTE();
  }
}
