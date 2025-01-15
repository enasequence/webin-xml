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
package uk.ac.ebi.ena.webin.xml.conversion.model.ena.common;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import java.util.List;
import lombok.Data;

@JsonPropertyOrder({"externalAccessions", "secondaryAccessions"})
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Identifiers {
  @JsonPropertyOrder({"db", "id"})
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class ExternalAccession {
    @JacksonXmlProperty(localName = "namespace", isAttribute = true)
    private String db;

    @JsonAlias("")
    @JacksonXmlText
    private String id;
  }

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "EXTERNAL_ID")
  private List<ExternalAccession> externalAccessions;

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "SECONDARY_ID")
  private List<String> secondaryAccessions;
}
