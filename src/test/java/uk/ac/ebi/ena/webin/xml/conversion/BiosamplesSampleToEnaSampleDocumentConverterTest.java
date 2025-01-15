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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.biosamples.model.Sample;
import uk.ac.ebi.ena.sra.xml.AttributeType;
import uk.ac.ebi.ena.sra.xml.SAMPLESETDocument;
import uk.ac.ebi.ena.sra.xml.SampleType;

public class BiosamplesSampleToEnaSampleDocumentConverterTest {

  private static final List<String> ATTRIBUTES_SKIPPED_BY_CONVERTER =
      Arrays.asList(
          "DATABASE NAME",
          "DATABASE ID",
          "DATABASE URI",
          "SAMPLE ACCESSION",
          "TERM SOURCE REF",
          "Organism",
          "description",
          "Sample description",
          "Sample_description",
          "title");

  private BiosamplesSampleToEnaSampleDocumentConverter converter =
      new BiosamplesSampleToEnaSampleDocumentConverter();

  public static Sample getDefaultTestSample() {
    return getBiosamplesSample("/uk/ac/ebi/ena/webin/xml/conversion/biosamples/sample.json");
  }

  @Test
  public void test() {
    String expectedAccession = "SAMEA110688704";
    long expectedTaxId = 2697049l;
    String expectedOrganism = "Severe acute respiratory syndrome coronavirus 2";
    String expectedTitle = "hCoV-19/Denmark/DCGC-563136/2022";
    String expectedSubmitterId = expectedTitle;
    String expectedSubmitterIdNamespace =
        "Statens Serum Institut and the Danish Covid19 Genome Consortium (DCGC)";

    Sample biosampleSSample = getDefaultTestSample();

    SAMPLESETDocument enaSampleDocument = converter.convert(biosampleSSample);

    Assert.assertNotNull(enaSampleDocument);
    Assert.assertEquals(1, enaSampleDocument.getSAMPLESET().getSAMPLEArray().length);

    SampleType sampleType = enaSampleDocument.getSAMPLESET().getSAMPLEArray(0);
    Assert.assertEquals(expectedAccession, sampleType.getAlias());
    Assert.assertEquals(
        expectedAccession, sampleType.getIDENTIFIERS().getSECONDARYIDArray(0).getStringValue());
    Assert.assertEquals(
        expectedSubmitterId, sampleType.getIDENTIFIERS().getSUBMITTERID().getStringValue());
    Assert.assertEquals(
        expectedSubmitterIdNamespace, sampleType.getIDENTIFIERS().getSUBMITTERID().getNamespace());
    Assert.assertEquals(expectedTitle, sampleType.getTITLE());
    Assert.assertNull(sampleType.getDESCRIPTION());

    SampleType.SAMPLENAME sampleNameType = sampleType.getSAMPLENAME();
    Assert.assertEquals(expectedTaxId, sampleNameType.getTAXONID());
    Assert.assertEquals(expectedOrganism, sampleNameType.getSCIENTIFICNAME());

    AttributeType[] enaSampleAttributes =
        sampleType.getSAMPLEATTRIBUTES().getSAMPLEATTRIBUTEArray();
    Assert.assertEquals(24, enaSampleAttributes.length);

    biosampleSSample
        .getAttributes()
        .forEach(
            biosamplesSampleAttribute -> {
              if (ATTRIBUTES_SKIPPED_BY_CONVERTER.stream()
                  .filter(
                      skippedAttributeName ->
                          skippedAttributeName.equalsIgnoreCase(
                              biosamplesSampleAttribute.getType()))
                  .findFirst()
                  .isEmpty()) {
                assertAttribute(
                    enaSampleAttributes,
                    biosamplesSampleAttribute.getType(),
                    biosamplesSampleAttribute.getValue());
              }
            });
  }

  private static Sample getBiosamplesSample(String resourcePath) {
    try (InputStream is =
        BiosamplesSampleToEnaSampleDocumentConverterTest.class.getResourceAsStream(resourcePath)) {
      ObjectMapper objectMapper = new ObjectMapper();

      return objectMapper.readValue(is, Sample.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void assertAttribute(
      AttributeType[] enaSampleAttributes,
      String expectedBiosamplesSampleAttributeName,
      String expectedBiosamplesSampleAttributeValue) {
    AttributeType enaSampleAttribute =
        Stream.of(enaSampleAttributes)
            .filter(
                enaSampleAtt -> expectedBiosamplesSampleAttributeName.equals(enaSampleAtt.getTAG()))
            .findFirst()
            .get();
    Assert.assertEquals(expectedBiosamplesSampleAttributeValue, enaSampleAttribute.getVALUE());
  }
}
