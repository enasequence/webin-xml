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
package uk.ac.ebi.ena.webin.xml.conversion;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import uk.ac.ebi.biosamples.model.Attribute;
import uk.ac.ebi.biosamples.model.Sample;
import uk.ac.ebi.ena.sra.xml.AttributeType;
import uk.ac.ebi.ena.sra.xml.QualifiedNameType;
import uk.ac.ebi.ena.sra.xml.SAMPLESETDocument;
import uk.ac.ebi.ena.sra.xml.SampleType;

// Code in this class was borrowed from uk.ac.ebi.ena.sra.biosamples.retrieval.BioSampleParser in
// sub-sra\sra-core.
public class BiosamplesSampleToEnaSampleDocumentConverter {

  private static final String ORGANISM = "Organism";
  private static final String DESCRIPTION = "description";
  private static final String DESCRIPTION1 = "Sample description";
  private static final String DESCRIPTION2 = "Sample_description";
  private static final String TITLE = "title";
  private static final List<String> EXCLUDED_PROPERTIES =
      Arrays.asList(
          "DATABASE NAME", "DATABASE ID", "DATABASE URI", "SAMPLE ACCESSION", "TERM SOURCE REF");

  public SAMPLESETDocument convert(Sample biosamplesSample) {
    SAMPLESETDocument enaSampleDocument = SAMPLESETDocument.Factory.newInstance();

    SampleType sampleType = enaSampleDocument.addNewSAMPLESET().addNewSAMPLE();
    sampleType.setAlias(biosamplesSample.getAccession());
    // TODO ask dipayan whether biosamples accession should be a primary id instead of secondary.
    // also ask him that if biosamples sample has SRA accession in it then should that be used as
    // secondary?
    sampleType
        .addNewIDENTIFIERS()
        .addNewSECONDARYID()
        .setStringValue(biosamplesSample.getAccession());

    setSubmitterId(biosamplesSample, sampleType);

    SampleType.SAMPLENAME sampleNameType = sampleType.addNewSAMPLENAME();

    setTaxonId(biosamplesSample, sampleNameType);

    setAttributes(biosamplesSample, sampleType, sampleNameType);

    return enaSampleDocument;
  }

  private void setSubmitterId(Sample biosamplesSample, SampleType sampleType) {
    Attribute submitterIdAttribute = getAttribute(biosamplesSample.getAttributes(), "Submitter Id");
    if (submitterIdAttribute != null) {
      QualifiedNameType submitterId = sampleType.getIDENTIFIERS().addNewSUBMITTERID();
      submitterId.setStringValue(submitterIdAttribute.getValue());

      if (submitterIdAttribute.getTag() != null) {
        String tag = submitterIdAttribute.getTag();
        if (tag.toLowerCase().startsWith("namespace:")
            || tag.toLowerCase().startsWith("namespace :")) {
          tag = tag.substring(tag.indexOf(":"));
          if (tag.length() > 1) {
            submitterId.setNamespace(tag.substring(1));
          }
        }
      }
    }
  }

  private void setTaxonId(Sample biosamplesSample, SampleType.SAMPLENAME sampleNameType) {
    sampleNameType.setTAXONID(0);

    try {
      final Long taxId = biosamplesSample.getTaxId();

      if (taxId != null && taxId != 0) {
        sampleNameType.setTAXONID(Math.toIntExact(taxId));
      }
    } catch (final Exception e) {
      /*Possibility of a int overflow, long has a bigger range than int*/
    }
  }

  private void setAttributes(
      Sample biosamplesSample, SampleType sampleType, SampleType.SAMPLENAME sampleNameType) {
    SampleType.SAMPLEATTRIBUTES sampleAttributesType = sampleType.addNewSAMPLEATTRIBUTES();
    boolean isSetTitle = false;

    for (Attribute attribute : biosamplesSample.getAttributes()) {
      String type = attribute.getType();

      if (ORGANISM.equalsIgnoreCase(type)) {
        sampleNameType.setSCIENTIFICNAME(attribute.getValue());
      } else if (DESCRIPTION.equalsIgnoreCase(type)
          || DESCRIPTION1.equalsIgnoreCase(type)
          || DESCRIPTION2.equalsIgnoreCase(type)) {
        // If title has not been set yet then use description as title.
        if (!isSetTitle) {
          sampleType.setTITLE(attribute.getValue());
          isSetTitle = true;
        } else {
          // TODO ask dipayan why description is not set alongside title above.
          sampleType.setDESCRIPTION(attribute.getValue());
        }
      } else if (TITLE.equalsIgnoreCase(type)) {
        sampleType.setTITLE(attribute.getValue());
        isSetTitle = true;
      } else if (!EXCLUDED_PROPERTIES.contains(type)) {
        AttributeType attr = sampleAttributesType.addNewSAMPLEATTRIBUTE();
        attr.setTAG(attribute.getType());
        attr.setVALUE(attribute.getValue());

        // Only set the UNITS if it's not null
        String unit = attribute.getUnit();
        if (unit != null) {
          attr.setUNITS(unit);
        }
      }
    }
  }

  private Attribute getAttribute(Set<Attribute> attributes, String attributeName) {
    return attributes.stream()
        .filter(attribute -> attribute.getType().equalsIgnoreCase(attributeName))
        .findFirst()
        .orElse(null);
  }
}
