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

public class PlatformEnumFixerTest {

  private PlatformEnumFixer platformEnumFixer = new PlatformEnumFixer();

  @Test
  public void test() {
    Assert.assertEquals("LS454", platformEnumFixer.fixValue("454 GS"));
    Assert.assertEquals("LS454", platformEnumFixer.getValue("454 GS"));

    Assert.assertEquals("LS454", platformEnumFixer.fixValue("454 gs"));
    Assert.assertEquals("LS454", platformEnumFixer.getValue("454 gs"));
  }
}
