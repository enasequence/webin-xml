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
package uk.ac.ebi.ena.webin.xml.conversion.json.model.common;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class XmlPlatform {
  private final Map<String, Map<String, String>> platformToInstrumentModelMap = new HashMap<>();

  @JsonAnyGetter
  public Map<String, Map<String, String>> getPlatformToInstrumentModelMap() {
    return platformToInstrumentModelMap;
  }

  @JsonAnySetter
  public void setPlatformToInstrumentModelMap(String name, Map<String, String> value) {
    this.platformToInstrumentModelMap.put(name, value);
  }
}
