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
package uk.ac.ebi.ena.webin.xml.conversion.json.model.receipt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@JsonPropertyOrder({
  "alias",
  "accession",
  "status",
  "centerName",
  "holdUntilDate",
  "externalAccession"
})
@Data
public class ReceiptObject {

  @JsonPropertyOrder({"accession", "type"})
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class ExternalAccession {
    @JacksonXmlProperty(localName = "accession", isAttribute = true)
    private String id;

    @JacksonXmlProperty(localName = "type", isAttribute = true)
    private String db;
  }

  @JacksonXmlProperty(localName = "alias", isAttribute = true)
  private String alias;

  @JacksonXmlProperty(localName = "accession", isAttribute = true)
  private String accession;

  @JacksonXmlProperty(localName = "status", isAttribute = true)
  private String status;

  @JacksonXmlProperty(localName = "centerName", isAttribute = true)
  private String centerName;

  @JacksonXmlProperty(localName = "holdUntilDate", isAttribute = true)
  private String holdUntilDate;

  @JacksonXmlProperty(localName = "EXT_ID")
  @JacksonXmlElementWrapper(useWrapping = false)
  private ExternalAccession externalAccession;
}
