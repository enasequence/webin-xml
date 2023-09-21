/*
 * Copyright 2021 EMBL - European Bioinformatics Institute
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package uk.ac.ebi.ena.webin.xml.transformation.fixers;

import org.apache.xmlbeans.SchemaStringEnumEntry;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

public class EnumValueFixer {
  private final Map<String, String> map = new HashMap<>();

  private static String normalize(String value) {
    if (value == null || value.isEmpty()) {
      return value;
    }

    return value.replaceAll(" ", "").toUpperCase();
  }

  protected void add(SchemaStringEnumEntry[] values) {
    for (SchemaStringEnumEntry value : values) {
      add(value.getString(), value.getString());
    }
  }

  protected void add(String value, String fixedValue) {
    String existingFixedValue = map.get(normalize(value));
    if (existingFixedValue != null
        && fixedValue != null
        && !existingFixedValue.equals(fixedValue)) {
      throw new RuntimeException(
          "Conflicting fixed values for "
              + value
              + ": "
              + existingFixedValue
              + " and "
              + fixedValue);
    }
    map.put(normalize(value), fixedValue);
  }

  public String fixValue(String value) {
    if (value == null) {
      return null;
    }

    String fixedValue = map.get(normalize(value));
    if (fixedValue == null) {
      return value;
    }
    return fixedValue;
  }

  public String getValue(String key) {
    if (key == null) {
      return null;
    }
    return map.get(normalize(key));
  }

  public void fixNodeValue(Node node) {
    String value = node.getTextContent();
    if (value == null) {
      return;
    }
    String fixedValue = fixValue(value);
    if (fixedValue != null && !value.equals(fixedValue)) {
      node.setTextContent(fixedValue);
    }
  }

  public Map<String, String> getMap() {
    return map;
  }
}
