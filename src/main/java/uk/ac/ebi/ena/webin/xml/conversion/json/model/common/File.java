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
package uk.ac.ebi.ena.webin.xml.conversion.json.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
  "filename",
  "fileType",
  "qualityScoringSystem",
  "checksum_method",
  "checksum",
  "readType"
})
@Data
public class File {
  @JacksonXmlProperty(localName = "filename", isAttribute = true)
  private String fileName;

  @JacksonXmlProperty(localName = "filetype", isAttribute = true)
  private String fileType;

  @JacksonXmlProperty(localName = "checksum_method", isAttribute = true)
  private String checksumMethod;

  @JacksonXmlProperty(localName = "checksum", isAttribute = true)
  private String checksum;

  @JacksonXmlElementWrapper(localName = "READ_TYPE", useWrapping = false)
  @JacksonXmlProperty(localName = "READ_TYPE")
  private List<String> readType;
}
