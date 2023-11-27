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
package uk.ac.ebi.ena.webin.xml.conversion.json.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import java.io.IOException;
import java.util.List;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.RefObject;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.experiment.LibraryDescriptor;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.experiment.XmlExperiment;

public class XmlDesignSerializer extends JsonSerializer<XmlExperiment.XmlDesign> {
  @Override
  public void serialize(
      final XmlExperiment.XmlDesign design,
      final JsonGenerator jsonGenerator,
      final SerializerProvider serializers)
      throws IOException {
    final ToXmlGenerator xmlGenerator;

    if (jsonGenerator instanceof ToXmlGenerator) {
      xmlGenerator = (ToXmlGenerator) jsonGenerator;

      xmlGenerator.writeStartObject();

      xmlGenerator.writeStringField("DESIGN_DESCRIPTION", design.getDesignDescription());

      final List<RefObject> samples = design.getSamples();

      for (final RefObject sample : samples) {
        xmlGenerator.writeFieldName("SAMPLE_DESCRIPTOR");
        xmlGenerator.writeStartObject(); // samples starts

        xmlGenerator.setNextIsAttribute(true);

        final String sampleAccession = sample.getAccession();

        if (sampleAccession != null) {
          xmlGenerator.writeStringField("accession", sampleAccession);
        }

        xmlGenerator.setNextIsAttribute(true);

        final String sampleAlias = sample.getAlias();

        if (sampleAlias != null) {
          xmlGenerator.writeStringField("refname", sampleAlias);
        }

        xmlGenerator.writeEndObject(); // samples ends
      }

      final LibraryDescriptor libraryDescriptor = design.getLibraryDescriptor();

      if (libraryDescriptor != null) {
        xmlGenerator.writeFieldName("LIBRARY_DESCRIPTOR");
        xmlGenerator.writeStartObject(); // libraryDescriptor starts

        xmlGenerator.writeFieldName("LIBRARY_NAME");
        xmlGenerator.writeString(libraryDescriptor.getLibraryName());

        xmlGenerator.writeFieldName("LIBRARY_STRATEGY");
        xmlGenerator.writeString(libraryDescriptor.getLibraryStrategy());

        xmlGenerator.writeFieldName("LIBRARY_SOURCE");
        xmlGenerator.writeString(libraryDescriptor.getLibrarySource());

        xmlGenerator.writeFieldName("LIBRARY_SELECTION");
        xmlGenerator.writeString(libraryDescriptor.getLibrarySelection());

        xmlGenerator.writeFieldName("LIBRARY_LAYOUT");

        final String libraryLayout = libraryDescriptor.getLibraryLayout();

        if (libraryLayout != null) {
          if (libraryLayout.equals("single")) {
            xmlGenerator.writeStartObject(); // libraryLayout starts
            xmlGenerator.writeFieldName("SINGLE");

            xmlGenerator.writeStartObject(); // single starts
            xmlGenerator.writeEndObject(); // single ends

            xmlGenerator.writeEndObject(); // libraryLayout ends
          } else if (libraryLayout.equalsIgnoreCase("paired")) {
            xmlGenerator.writeStartObject(); // libraryLayout starts
            xmlGenerator.writeFieldName("PAIRED");

            xmlGenerator.writeStartObject(); // paired starts
            xmlGenerator.writeEndObject(); // paired ends

            xmlGenerator.writeEndObject(); // libraryLayout ends
          } else {
            throw new RuntimeException("Invalid library layout type");
          }
        }

        // TODO: add pooling strategy and library construction protocol

        xmlGenerator.writeEndObject(); // libraryDescriptor ends
      }

      xmlGenerator.writeEndObject();
    }
  }
}
