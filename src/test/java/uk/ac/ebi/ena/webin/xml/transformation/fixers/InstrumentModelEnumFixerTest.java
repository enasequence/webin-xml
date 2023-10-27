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
package uk.ac.ebi.ena.webin.xml.transformation.fixers;

import org.junit.Assert;
import org.junit.Test;

public class InstrumentModelEnumFixerTest {

  private InstrumentModelEnumFixer instrumentModelEnumFixer = new InstrumentModelEnumFixer();

  @Test
  public void testFixCase() {
    String withoutExplicitFix = "HiSeq X Five";
    String withoutExplicitFixWrongCase = "hIsEQ x fIVE";

    // The fix of a value that does not have an explicit fix defined for it is the value itself.
    String fixedValue = withoutExplicitFix;

    Assert.assertEquals(fixedValue, instrumentModelEnumFixer.fixValue(withoutExplicitFix));
    Assert.assertEquals(fixedValue, instrumentModelEnumFixer.getValue(withoutExplicitFix));

    Assert.assertEquals(fixedValue, instrumentModelEnumFixer.fixValue(withoutExplicitFixWrongCase));
    Assert.assertEquals(fixedValue, instrumentModelEnumFixer.getValue(withoutExplicitFixWrongCase));
  }

  @Test
  public void testFixModel() {
    assertFixedValue("none", "unspecified");
    assertFixedValue("AB SOLiD System 3 Plus", "AB SOLiD 3 Plus System");
    assertFixedValue("AB SOLiD 5500xl", "AB 5500xl Genetic Analyzer");
    assertFixedValue("AB SOLiD 5500", "AB 5500 Genetic Analyzer");
    assertFixedValue("GS FLX", "454 GS FLX");
    assertFixedValue("GS 20", "454 GS 20");
    assertFixedValue("454 Titanium", "454 GS FLX Titanium");
    assertFixedValue("454 GS FLX Plus", "454 GS FLX+");
    assertFixedValue("Solexa 1G Genome Analyzer", "Illumina Genome Analyzer");
    assertFixedValue("Illumina NextSeq 500", "NextSeq 500");
    assertFixedValue("Illumina NextSeq 550", "NextSeq 550");
    assertFixedValue("Illumina HiSeq X Ten", "HiSeq X Ten");
    assertFixedValue("Illumina HiSeq X Five", "HiSeq X Five");
    assertFixedValue("Ion S5", "Ion Torrent S5");
    assertFixedValue("Ion S5 XL", "Ion Torrent S5 XL");
    assertFixedValue("Ion S5", "Ion Torrent S5");
    assertFixedValue("PacBio Sequel", "Sequel");
    assertFixedValue("PacBio Sequel II", "Sequel II");
  }

  public void assertFixedValue(String actualValue, String expectedValue) {
    Assert.assertEquals(expectedValue, instrumentModelEnumFixer.fixValue(actualValue));
    Assert.assertEquals(expectedValue, instrumentModelEnumFixer.getValue(actualValue));
    Assert.assertEquals(
        expectedValue, instrumentModelEnumFixer.fixValue(actualValue.toUpperCase()));
    Assert.assertEquals(
        expectedValue, instrumentModelEnumFixer.getValue(actualValue.toUpperCase()));
    Assert.assertEquals(
        expectedValue, instrumentModelEnumFixer.fixValue(actualValue.toLowerCase()));
    Assert.assertEquals(
        expectedValue, instrumentModelEnumFixer.getValue(actualValue.toUpperCase()));
  }
}
