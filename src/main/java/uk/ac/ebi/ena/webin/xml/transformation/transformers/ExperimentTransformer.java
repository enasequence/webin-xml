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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import uk.ac.ebi.ena.sra.xml.*;
import uk.ac.ebi.ena.webin.xml.transformation.WebinXmlTransformationException;
import uk.ac.ebi.ena.webin.xml.transformation.fixers.InstrumentModelEnumFixer;
import uk.ac.ebi.ena.webin.xml.transformation.fixers.LibrarySourceEnumFixer;
import uk.ac.ebi.ena.webin.xml.transformation.fixers.PlatformEnumFixer;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.dtos.ExperimentTransformationDTO;

public class ExperimentTransformer extends AbstractTransformer
    implements Transformer<ExperimentTransformationDTO, EXPERIMENTSETDocument> {

  private final InstrumentModelEnumFixer instrumentModelEnumFixer = new InstrumentModelEnumFixer();
  private final PlatformEnumFixer platformEnumFixer = new PlatformEnumFixer();
  private final LibrarySourceEnumFixer librarySourceEnumFixer = new LibrarySourceEnumFixer();

  public ExperimentTransformer(Templates transformationTemplate) {
    super(transformationTemplate);
  }

  @Override
  public Document transform(Document document) throws WebinXmlTransformationException {
    try {
      document = applyTemplateTransformation(document);

      // Add library strategy ('OTHER') if missing.
      for (Node libraryDescriptor :
          getXmlNodes(
              document,
              "/EXPERIMENT_SET/EXPERIMENT/DESIGN/LIBRARY_DESCRIPTOR[not(LIBRARY_STRATEGY)]")) {
        Node librarySource = getXmlNode(libraryDescriptor, "LIBRARY_SOURCE");
        if (librarySource != null) {
          Element libraryStrategy = document.createElement("LIBRARY_STRATEGY");
          libraryStrategy.setTextContent("OTHER");
          libraryDescriptor.insertBefore(libraryStrategy, librarySource);
        }
      }

      for (Node libraryStrategy :
          getXmlNodes(
              document,
              "/EXPERIMENT_SET/EXPERIMENT/DESIGN/LIBRARY_DESCRIPTOR/LIBRARY_STRATEGY[text()='NON GENOMIC']")) {
        libraryStrategy.setTextContent("OTHER");
      }
      for (Node libraryStrategy :
          getXmlNodes(
              document,
              "/EXPERIMENT_SET/EXPERIMENT/DESIGN/LIBRARY_DESCRIPTOR/LIBRARY_STRATEGY[text()='other']")) {
        libraryStrategy.setTextContent("OTHER");
      }

      for (Node librarySelection :
          getXmlNodes(
              document,
              "/EXPERIMENT_SET/EXPERIMENT/DESIGN/LIBRARY_DESCRIPTOR/LIBRARY_SELECTION[text()='DNAse']")) {
        librarySelection.setTextContent("DNase");
      }
      for (Node librarySelection :
          getXmlNodes(
              document,
              "/EXPERIMENT_SET/EXPERIMENT/DESIGN/LIBRARY_DESCRIPTOR/LIBRARY_SELECTION[text()='OTHER']")) {
        librarySelection.setTextContent("other");
      }

      fixInstrumentModel(document, "/EXPERIMENT_SET/EXPERIMENT/PLATFORM");
      fixPlatform(document);
      fixLibrarySource(document);

      return document;
    } catch (TransformerException e) {
      throw new WebinXmlTransformationException("Error applying experiment transformation.", e);
    } catch (Exception e) {
      throw new WebinXmlTransformationException(
          "Unexpected error while transforming experiment xml.", e);
    }
  }

  @Override
  public EXPERIMENTSETDocument transformForPresentation(
      ExperimentTransformationDTO experimentDto, EXPERIMENTSETDocument experimentSetDocument)
      throws WebinXmlTransformationException {

    ExperimentType experimentType =
        experimentSetDocument.getEXPERIMENTSET().getEXPERIMENTArray()[0];

    transformCommon(experimentDto, experimentDto.getExperimentId(), experimentType);

    transformIdentifiers(experimentDto, experimentType);

    if (experimentDto.getExperimentId().startsWith("ERX"))
      expandSamples(experimentType, experimentDto.getSampleAccessions());

    // Generated Title
    updateTitle(
        experimentType,
        null == experimentDto.getInstrumentModel() ? "" : experimentDto.getInstrumentModel());

    transformLinks(experimentDto, experimentType);

    transformAttributes(experimentDto, experimentType);

    return experimentSetDocument;
  }

  private void fixInstrumentModel(Node doc, String platformPath) {
    for (Node instrumentModel : getXmlNodes(doc, platformPath + "/*/INSTRUMENT_MODEL")) {
      instrumentModelEnumFixer.fixNodeValue(instrumentModel);
    }
  }

  private void fixPlatform(Document doc) {
    String platformPath = "/EXPERIMENT_SET/EXPERIMENT/PLATFORM";
    List<Node> platforms = getXmlNodes(doc, platformPath + "/*");
    for (Node pl : platforms) {
      if (pl != null) {
        String fixedPlatform = null;
        for (Node instrumentModel :
            getXmlNodes(doc, platformPath + "/" + pl.getNodeName() + "/INSTRUMENT_MODEL")) {
          fixedPlatform = platformEnumFixer.getValue(instrumentModel.getTextContent());
        }
        if (fixedPlatform != null && !fixedPlatform.equalsIgnoreCase(pl.getNodeName()))
          doc.renameNode(pl, pl.getNamespaceURI(), fixedPlatform);
      }
    }
  }

  private void fixLibrarySource(Node doc) {
    for (Node librarySource :
        getXmlNodes(doc, "/EXPERIMENT_SET/EXPERIMENT/DESIGN/LIBRARY_DESCRIPTOR/LIBRARY_SOURCE")) {
      if (librarySource != null) {
        librarySourceEnumFixer.fixNodeValue(librarySource);
      }
    }
  }

  private void transformIdentifiers(
      ExperimentTransformationDTO experimentDto, ExperimentType experimentType) {
    if (experimentDto.getExperimentId().startsWith("ERX"))
      injectSecondaries(experimentType.getIDENTIFIERS(), experimentDto.getSecondary());

    fixIdentifiers(experimentType);
  }

  private void expandSamples(ExperimentType experimentType, Map<String, String> sample_list) {
    if (experimentType != null
        && experimentType.getDESIGN() != null
        && experimentType.getDESIGN().getSAMPLEDESCRIPTOR() != null
        && experimentType.getDESIGN().getSAMPLEDESCRIPTOR().getIDENTIFIERS() != null) {
      IdentifierType identifierType =
          experimentType.getDESIGN().getSAMPLEDESCRIPTOR().getIDENTIFIERS();
      if (identifierType != null
          && sample_list.containsKey(identifierType.getPRIMARYID().getStringValue())) {
        QualifiedNameType qualifiedNameType = identifierType.addNewEXTERNALID();
        qualifiedNameType.setNamespace("BioSample");
        qualifiedNameType.setStringValue(
            sample_list.get(identifierType.getPRIMARYID().getStringValue()));
      }

      if (experimentType.getDESIGN().getSAMPLEDESCRIPTOR().getPOOL() != null) {
        SampleDescriptorType.POOL pool = experimentType.getDESIGN().getSAMPLEDESCRIPTOR().getPOOL();
        PoolMemberType[] memberArray = pool.getMEMBERArray();
        for (int i = 0; i < memberArray.length; i++) {
          IdentifierType identifiers1 = memberArray[i].getIDENTIFIERS();
          if (identifiers1 != null) {
            QualifiedNameType qualifiedNameType = identifierType.addNewEXTERNALID();
            if (sample_list.containsKey(identifiers1.getPRIMARYID().getStringValue())) {
              qualifiedNameType.setNamespace("BioSample");
              qualifiedNameType.setStringValue(
                  sample_list.get(identifierType.getPRIMARYID().getStringValue()));
            }
          }
        }
      }
    }
  }

  private void updateTitle(ExperimentType experimentType, String generatedTitle) {
    if (experimentType.getDESIGN() != null
        && experimentType.getDESIGN().getLIBRARYDESCRIPTOR() != null
        && experimentType.getDESIGN().getLIBRARYDESCRIPTOR().getLIBRARYLAYOUT() != null) {
      if (experimentType.getDESIGN().getLIBRARYDESCRIPTOR().getLIBRARYLAYOUT().getPAIRED() != null)
        generatedTitle = generatedTitle + " paired end";
    }

    generatedTitle = generatedTitle + " sequencing";

    String xmlTitle = experimentType.getTITLE();
    if (xmlTitle == null || xmlTitle.replaceAll(" ", "").isEmpty()) {
      if (generatedTitle != null) {
        experimentType.setTITLE(generatedTitle);
      }
    }
  }

  private void transformLinks(
      ExperimentTransformationDTO experimentDto, ExperimentType experimentType) {

    retainOnlyPubmedLinks(
        experimentType.getEXPERIMENTLINKS(),
        experimentType.getEXPERIMENTLINKS() != null
            ? experimentType.getEXPERIMENTLINKS().getEXPERIMENTLINKArray()
            : null);

    if (!experimentDto.getSampleIds().isEmpty())
      appendExperimentLink(
          experimentType,
          "ENA-SAMPLE",
          getRangeList(experimentDto.getSampleIds()).stream().collect(Collectors.joining(",")));

    if (null != experimentDto.getSubmissionId())
      appendExperimentLink(experimentType, "ENA-SUBMISSION", experimentDto.getSubmissionId());

    // FASTQ files
    appendExperimentLink(
        experimentType,
        ENA_FASTQ_FILES_TAG,
        String.format(ENA_FASTQ_FILES_URL_PREFORMAT, experimentDto.getExperimentId()),
        true);

    // Submitted files
    appendExperimentLink(
        experimentType,
        ENA_SUBMITTED_FILES_TAG,
        String.format(ENA_SUBMITTED_FILES_URL_PREFORMAT, experimentDto.getExperimentId()),
        true);

    appendArrayExpressLink(experimentDto.getStudyAlias(), () -> createNewLinkXRef(experimentType));
  }

  private void transformAttributes(
      ExperimentTransformationDTO experimentDto, ExperimentType experimentType) {
    addEnaStatusIdAttribute(experimentDto, () -> createNewAttribute(experimentType));
  }

  private void appendExperimentLink(ExperimentType experimentType, String db, String id) {
    appendExperimentLink(experimentType, db, id, false);
  }

  private void appendExperimentLink(
      ExperimentType experimentType, String db, String id, boolean cdata) {
    appendLink(createNewLinkXRef(experimentType), db, id, cdata);
  }

  private XRefType createNewLinkXRef(ExperimentType experimentType) {
    ExperimentType.EXPERIMENTLINKS links = experimentType.getEXPERIMENTLINKS();
    links = links == null ? experimentType.addNewEXPERIMENTLINKS() : links;

    return links.addNewEXPERIMENTLINK().addNewXREFLINK();
  }

  private AttributeType createNewAttribute(ExperimentType experimentType) {
    ExperimentType.EXPERIMENTATTRIBUTES attributes = experimentType.getEXPERIMENTATTRIBUTES();
    if (attributes == null) {
      attributes = experimentType.addNewEXPERIMENTATTRIBUTES();
    }

    return attributes.addNewEXPERIMENTATTRIBUTE();
  }
}
