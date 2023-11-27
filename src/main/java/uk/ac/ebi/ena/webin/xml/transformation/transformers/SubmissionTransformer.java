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

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import uk.ac.ebi.ena.sra.xml.SUBMISSIONSETDocument;
import uk.ac.ebi.ena.sra.xml.SubmissionType;
import uk.ac.ebi.ena.sra.xml.XRefType;
import uk.ac.ebi.ena.webin.xml.transformation.WebinXmlTransformationException;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.dtos.SubmissionTransformationDTO;

public class SubmissionTransformer extends AbstractTransformer
    implements Transformer<SubmissionTransformationDTO, SUBMISSIONSETDocument> {

  public SubmissionTransformer(Templates transformationTemplate) {
    super(transformationTemplate);
  }

  @Override
  public Document transform(Document document) throws WebinXmlTransformationException {
    try {
      document = applyTemplateTransformation(document);

      return document;
    } catch (TransformerException e) {
      throw new WebinXmlTransformationException("Error applying submission transformation.", e);
    } catch (Exception e) {
      throw new WebinXmlTransformationException(
          "Unexpected error while transforming submission xml.", e);
    }
  }

  @Override
  public SUBMISSIONSETDocument transformForPresentation(
      SubmissionTransformationDTO submissionDto, SUBMISSIONSETDocument submissionSetDocument)
      throws WebinXmlTransformationException {

    SubmissionType submissionType =
        submissionSetDocument.getSUBMISSIONSET().getSUBMISSIONArray()[0];

    transformCommon(submissionDto, submissionDto.getSubmissionId(), submissionType);

    // Remove submission actions.
    if (submissionType.isSetACTIONS()) {
      Node nodeA = submissionType.getACTIONS().getDomNode();
      submissionType.getDomNode().removeChild(nodeA);
    }

    // Remove submission contacts.
    if (submissionType.isSetCONTACTS()) {
      Node nodeC = submissionType.getCONTACTS().getDomNode();
      submissionType.getDomNode().removeChild(nodeC);
    }

    // Avoid emails in comments
    if (null != submissionType.getSubmissionComment()
        && (submissionType.getSubmissionComment().isEmpty()
            || submissionType.getSubmissionComment().contains("@")))
      submissionType.unsetSubmissionComment();

    transformIdentifiers(submissionDto, submissionType);

    updateTitle(submissionDto, submissionType);

    transformLinks(submissionDto, submissionType);

    return submissionSetDocument;
  }

  private void transformIdentifiers(
      SubmissionTransformationDTO submissionDto, SubmissionType submissionType) {

    unsetIdentifiersSubmitterIdIfBlankAlias(submissionType);

    fixIdentifiers(submissionType);
  }

  private void updateTitle(
      SubmissionTransformationDTO submissionDto, SubmissionType submissionType) {
    // SUBMISSION_TITLE
    String genratedTitle = "Submitted";

    if (null != submissionDto.getCenterName() && !submissionDto.getCenterName().isEmpty())
      genratedTitle = genratedTitle + " by " + submissionDto.getCenterName();

    if (null != submissionDto.getSubmissionDate()) {
      genratedTitle = genratedTitle + " on " + submissionDto.getSubmissionDate().toUpperCase();
    }

    if (genratedTitle != null) {
      submissionType.setTITLE(genratedTitle);
    }
  }

  private void transformLinks(
      SubmissionTransformationDTO submissionDto, SubmissionType submissionType) {

    retainOnlyPubmedLinks(
        submissionType.getSUBMISSIONLINKS(),
        submissionType.getSUBMISSIONLINKS() != null
            ? submissionType.getSUBMISSIONLINKS().getSUBMISSIONLINKArray()
            : null);

    // Fastq files
    appendSubmissionLink(
        submissionType,
        ENA_FASTQ_FILES_TAG,
        String.format(ENA_FASTQ_FILES_URL_PREFORMAT, submissionDto.getSubmissionId()),
        true);

    // Submitted files
    appendSubmissionLink(
        submissionType,
        ENA_SUBMITTED_FILES_TAG,
        String.format(ENA_SUBMITTED_FILES_URL_PREFORMAT, submissionDto.getSubmissionId()),
        true);

    appendArrayExpressLink(
        submissionDto.getSubmissionAlias(), () -> createNewLinkXRef(submissionType));
  }

  private void appendSubmissionLink(
      SubmissionType submissionType, String db, String id, boolean cdata) {
    appendLink(createNewLinkXRef(submissionType), db, id, cdata);
  }

  private XRefType createNewLinkXRef(SubmissionType submissionType) {
    SubmissionType.SUBMISSIONLINKS links = submissionType.getSUBMISSIONLINKS();
    links = links == null ? submissionType.addNewSUBMISSIONLINKS() : links;

    return links.addNewSUBMISSIONLINK().addNewXREFLINK();
  }
}
