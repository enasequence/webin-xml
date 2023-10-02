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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EnumValueFixerTest {

  private EnumValueFixer[] fixers = {new InstrumentModelEnumFixer(), new LibrarySourceEnumFixer()};

  @Test
  public void testFixValue_Fix() {
    for (EnumValueFixer fixer : fixers) {
      fixer
          .getMap()
          .forEach(
              (value, fixedValue) -> {
                Assert.assertEquals(fixedValue, fixer.fixValue(value.toLowerCase()));
                Assert.assertEquals(fixedValue, fixer.fixValue(value.toUpperCase()));
                Assert.assertEquals(fixedValue, fixer.fixValue(value.replaceAll(" ", "  ")));
              });
    }
  }

  @Test
  public void testFixValue_NoFix() {
    for (EnumValueFixer fixer : fixers) {
      Assert.assertEquals("test 1", fixer.fixValue("test 1"));
      Assert.assertEquals("TEST 1", fixer.fixValue("TEST 1"));
      Assert.assertEquals("Test 1", fixer.fixValue("Test 1"));
    }
  }

  @Test
  public void testFixValue_Null() {
    for (EnumValueFixer fixer : fixers) {
      Assert.assertNull(fixer.fixValue(null));
    }
  }

  @Test
  public void testFixNodeValue_Fix() {
    for (EnumValueFixer fixer : fixers) {
      fixer
          .getMap()
          .forEach(
              (value, fixedValue) -> {
                DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
                try {
                  DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
                  Document document = documentBuilder.newDocument();
                  Element element = document.createElement("test");
                  document.appendChild(element);

                  element.setTextContent(value.toLowerCase());
                  fixer.fixNodeValue(element);
                  Assert.assertEquals(fixedValue, element.getTextContent());

                  element.setTextContent(value.toUpperCase());
                  fixer.fixNodeValue(element);
                  Assert.assertEquals(fixedValue, element.getTextContent());

                  element.setTextContent(value.replaceAll(" ", "  "));
                  fixer.fixNodeValue(element);
                  Assert.assertEquals(fixedValue, element.getTextContent());
                } catch (Exception ex) {
                  throw new RuntimeException(ex);
                }
              });
    }
  }

  @Test
  public void testFixNodeValue_NoFix() {
    for (EnumValueFixer fixer : fixers) {
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      try {
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element element = document.createElement("test");
        document.appendChild(element);

        for (String value : new String[] {"test 1", "TEST 1", "Test 1"}) {
          element.setTextContent(value);
          fixer.fixNodeValue(element);
          Assert.assertEquals(value, element.getTextContent());
        }
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  @Test
  public void testFixNodeValue_Null() {
    for (EnumValueFixer fixer : fixers) {
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      try {
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element element = document.createElement("test");
        document.appendChild(element);

        fixer.fixNodeValue(element);
        Assert.assertEquals("", element.getTextContent());
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
  }
}
