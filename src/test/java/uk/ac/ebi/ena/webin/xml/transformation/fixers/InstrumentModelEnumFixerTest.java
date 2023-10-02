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
  public void test() {
    String withoutExplicitFix = "HiSeq X Five";
    String withoutExplicitFixWrongCase = "hIsEQ x fIVE";

    // The fix of a value that does not have an explicit fix defined for it is the value itself.
    String fixedValue = withoutExplicitFix;

    Assert.assertEquals(fixedValue, instrumentModelEnumFixer.fixValue(withoutExplicitFix));
    Assert.assertEquals(fixedValue, instrumentModelEnumFixer.getValue(withoutExplicitFix));

    Assert.assertEquals(fixedValue, instrumentModelEnumFixer.fixValue(withoutExplicitFixWrongCase));
    Assert.assertEquals(fixedValue, instrumentModelEnumFixer.getValue(withoutExplicitFixWrongCase));

    String withExplicitFix = "AB SOLiD 5500xl";
    String withExplicitFixWrongCase = "ab solId 5500XL";

    fixedValue = "AB 5500xl Genetic Analyzer";

    Assert.assertEquals(fixedValue, instrumentModelEnumFixer.fixValue(withExplicitFix));
    Assert.assertEquals(fixedValue, instrumentModelEnumFixer.getValue(withExplicitFix));

    Assert.assertEquals(fixedValue, instrumentModelEnumFixer.fixValue(withExplicitFixWrongCase));
    Assert.assertEquals(fixedValue, instrumentModelEnumFixer.getValue(withExplicitFixWrongCase));
  }
}
