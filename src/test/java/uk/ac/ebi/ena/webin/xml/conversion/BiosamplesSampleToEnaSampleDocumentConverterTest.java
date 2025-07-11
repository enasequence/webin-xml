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

  public static Sample loadSampleFromFile(String fileName) {
    return getBiosamplesSample("/uk/ac/ebi/ena/webin/xml/conversion/biosamples/" + fileName);
  }

  @Test
  public void test01() {
    String expectedAlias = "hCoV-19/Denmark/DCGC-563136/2022";
    String expectedAccession = "SAMEA110688704";
    String expectedPrimaryId = expectedAccession;
    String expectedSubmitterId = expectedAlias;
    long expectedTaxId = 2697049l;
    String expectedOrganism = "Severe acute respiratory syndrome coronavirus 2";
    String expectedTitle = expectedAlias;
    String expectedDescription = "Original";

    Sample biosampleSSample = loadSampleFromFile("sample01.json");

    SAMPLESETDocument enaSampleDocument = converter.convert(biosampleSSample);

    Assert.assertNotNull(enaSampleDocument);
    Assert.assertEquals(1, enaSampleDocument.getSAMPLESET().getSAMPLEArray().length);

    SampleType sampleType = enaSampleDocument.getSAMPLESET().getSAMPLEArray(0);
    Assert.assertEquals(expectedAlias, sampleType.getAlias());
    Assert.assertEquals(expectedAccession, sampleType.getAccession());
    Assert.assertEquals(
        expectedPrimaryId, sampleType.getIDENTIFIERS().getPRIMARYID().getStringValue());
    Assert.assertEquals(0, sampleType.getIDENTIFIERS().getSECONDARYIDArray().length);
    Assert.assertEquals(
        expectedSubmitterId, sampleType.getIDENTIFIERS().getSUBMITTERID().getStringValue());
    Assert.assertEquals(0, sampleType.getIDENTIFIERS().getEXTERNALIDArray().length);
    Assert.assertEquals(expectedTitle, sampleType.getTITLE());
    Assert.assertEquals(expectedDescription, sampleType.getDESCRIPTION());

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

  @Test
  public void test02() {
    String expectedAlias = "INPT#4";
    String expectedAccession = "SAMN00849598";
    String expectedPrimaryId = expectedAccession;
    String expectedSubmitterId = expectedAlias;
    long expectedTaxId = 433733l;
    String expectedOrganism = "human lung metagenome";
    String expectedTitle = "GAO_CF_Bv6v4 INPT#4";
    String expectedDescription = expectedOrganism;

    Sample biosampleSSample = loadSampleFromFile("sample02.json");

    SAMPLESETDocument enaSampleDocument = converter.convert(biosampleSSample);

    Assert.assertNotNull(enaSampleDocument);
    Assert.assertEquals(1, enaSampleDocument.getSAMPLESET().getSAMPLEArray().length);

    SampleType sampleType = enaSampleDocument.getSAMPLESET().getSAMPLEArray(0);
    Assert.assertEquals(expectedAlias, sampleType.getAlias());
    Assert.assertEquals(expectedAccession, sampleType.getAccession());
    Assert.assertEquals(
        expectedPrimaryId, sampleType.getIDENTIFIERS().getPRIMARYID().getStringValue());
    Assert.assertEquals(0, sampleType.getIDENTIFIERS().getSECONDARYIDArray().length);
    Assert.assertEquals(
        expectedSubmitterId, sampleType.getIDENTIFIERS().getSUBMITTERID().getStringValue());
    Assert.assertEquals(0, sampleType.getIDENTIFIERS().getEXTERNALIDArray().length);
    Assert.assertEquals(expectedTitle, sampleType.getTITLE());
    Assert.assertEquals(expectedDescription, sampleType.getDESCRIPTION());

    SampleType.SAMPLENAME sampleNameType = sampleType.getSAMPLENAME();
    Assert.assertEquals(expectedTaxId, sampleNameType.getTAXONID());
    Assert.assertEquals(expectedOrganism, sampleNameType.getSCIENTIFICNAME());

    AttributeType[] enaSampleAttributes =
        sampleType.getSAMPLEATTRIBUTES().getSAMPLEATTRIBUTEArray();
    Assert.assertEquals(9, enaSampleAttributes.length);

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
