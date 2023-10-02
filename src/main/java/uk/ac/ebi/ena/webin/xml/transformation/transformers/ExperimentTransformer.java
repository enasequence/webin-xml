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
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import uk.ac.ebi.ena.sra.xml.STUDYSETDocument;
import uk.ac.ebi.ena.webin.xml.transformation.WebinXmlTransformationException;
import uk.ac.ebi.ena.webin.xml.transformation.fixers.InstrumentModelEnumFixer;
import uk.ac.ebi.ena.webin.xml.transformation.fixers.LibrarySourceEnumFixer;
import uk.ac.ebi.ena.webin.xml.transformation.fixers.PlatformEnumFixer;

public class ExperimentTransformer extends AbstractTransformer {

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
  public STUDYSETDocument transformForPresentation(Document document)
      throws WebinXmlTransformationException {
    return null;
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
}
