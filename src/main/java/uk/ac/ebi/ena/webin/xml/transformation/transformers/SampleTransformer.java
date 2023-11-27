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
import uk.ac.ebi.ena.sra.xml.AttributeType;
import uk.ac.ebi.ena.sra.xml.QualifiedNameType;
import uk.ac.ebi.ena.sra.xml.SAMPLESETDocument;
import uk.ac.ebi.ena.sra.xml.SampleType;
import uk.ac.ebi.ena.sra.xml.XRefType;
import uk.ac.ebi.ena.webin.xml.transformation.WebinXmlTransformationException;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.dtos.SampleTransformationDTO;

public class SampleTransformer extends AbstractTransformer
    implements Transformer<SampleTransformationDTO, SAMPLESETDocument> {

  public SampleTransformer(Templates transformationTemplate) {
    super(transformationTemplate);
  }

  @Override
  public Document transform(Document document) throws WebinXmlTransformationException {
    try {
      document = applyTemplateTransformation(document);

      return document;
    } catch (TransformerException e) {
      throw new WebinXmlTransformationException("Error applying sample transformation.", e);
    } catch (Exception e) {
      throw new WebinXmlTransformationException(
          "Unexpected error while transforming sample xml.", e);
    }
  }

  @Override
  public SAMPLESETDocument transformForPresentation(
      SampleTransformationDTO sampleDto, SAMPLESETDocument sampleSetDocument)
      throws WebinXmlTransformationException {

    SampleType sampleType = sampleSetDocument.getSAMPLESET().getSAMPLEArray(0);

    transformCommon(sampleDto, sampleDto.getSampleId(), sampleType);

    transformIdentifiers(sampleDto, sampleType);

    // Generated TITLE
    if (sampleType.getSAMPLENAME() != null && sampleType.getSAMPLENAME().getTAXONID() != 0)
      generateTitle(sampleType);

    transformLinks(sampleDto, sampleType);

    transformAttributes(sampleDto, sampleType);

    return sampleSetDocument;
  }

  private void transformIdentifiers(SampleTransformationDTO sampleDto, SampleType sampleType) {

    unsetIdentifiersSubmitterIdIfBlankAlias(sampleType);

    if (sampleType.isSetIDENTIFIERS()) {
      // append BioSample identifier
      if (sampleDto.getSampleId().startsWith("ERS") && sampleDto.getBioSampleId() != null) {
        QualifiedNameType bioSampleNameType = sampleType.getIDENTIFIERS().addNewEXTERNALID();
        bioSampleNameType.setStringValue(sampleDto.getBioSampleId());
        bioSampleNameType.setNamespace("BioSample");
      }
    }

    if (sampleDto.getSampleId().startsWith("ERS"))
      injectSecondaries(sampleType.getIDENTIFIERS(), sampleDto.getSecondary());

    fixIdentifiers(sampleType);
  }

  /** Get fixed tax id of the sample XML from DB and inject that to the sample XML */
  private void generateTitle(SampleType sampleType) {
    String title = sampleType.getTITLE();
    if (null == title || title.isEmpty()) {
      String generatedOrganismNameFromTaxId = sampleType.getSAMPLENAME().getSCIENTIFICNAME();
      if (generatedOrganismNameFromTaxId != null)
        sampleType.setTITLE(generatedOrganismNameFromTaxId);
    }
  }

  private void transformLinks(SampleTransformationDTO sampleDto, SampleType sampleType) {
    retainOnlyPubmedLinks(
        sampleType.getSAMPLELINKS(),
        sampleType.getSAMPLELINKS() != null
            ? sampleType.getSAMPLELINKS().getSAMPLELINKArray()
            : null);

    if (sampleDto.getSubmissionId() != null)
      appendSampleLink(sampleType, "ENA-SUBMISSION", sampleDto.getSubmissionId());

    appendArrayExpressLink(sampleDto.getSubmissionAlias(), () -> createNewLinkXRef(sampleType));

    // FASTQ files
    appendSampleLink(
        sampleType,
        ENA_FASTQ_FILES_TAG,
        String.format(ENA_FASTQ_FILES_URL_PREFORMAT, sampleDto.getSampleId()),
        true);

    // Submitted files
    appendSampleLink(
        sampleType,
        ENA_SUBMITTED_FILES_TAG,
        String.format(ENA_SUBMITTED_FILES_URL_PREFORMAT, sampleDto.getSampleId()),
        true);
  }

  private void transformAttributes(SampleTransformationDTO sampleDto, SampleType sampleType) {
    addFirstPublicLastUpdateAttributes(sampleDto, () -> createNewAttribute(sampleType));
  }

  private void appendSampleLink(SampleType sampleType, String db, String id) {
    appendSampleLink(sampleType, db, id, false);
  }

  private void appendSampleLink(SampleType sampleType, String db, String id, boolean cdata) {
    appendLink(createNewLinkXRef(sampleType), db, id, cdata);
  }

  private XRefType createNewLinkXRef(SampleType sampleType) {
    SampleType.SAMPLELINKS links = sampleType.getSAMPLELINKS();
    links = links == null ? sampleType.addNewSAMPLELINKS() : links;

    return links.addNewSAMPLELINK().addNewXREFLINK();
  }

  private AttributeType createNewAttribute(SampleType sampleType) {
    SampleType.SAMPLEATTRIBUTES attributes = sampleType.getSAMPLEATTRIBUTES();
    if (attributes == null) {
      attributes = sampleType.addNewSAMPLEATTRIBUTES();
    }

    return attributes.addNewSAMPLEATTRIBUTE();
  }
}
