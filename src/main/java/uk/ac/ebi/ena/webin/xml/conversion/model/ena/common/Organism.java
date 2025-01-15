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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"taxonId", "commonName", "scientificName"})
@Data
public class Organism {
  @JacksonXmlProperty(localName = "TAXON_ID")
  private String taxonId;

  @JacksonXmlProperty(localName = "COMMON_NAME")
  private String commonName;

  @JacksonXmlProperty(localName = "SCIENTIFIC_NAME")
  private String scientificName;

  // For backward compatibility with old XML only.
  @JacksonXmlProperty(localName = "ANONYMIZED_NAME")
  private String anonymizedName;

  // For backward compatibility with old XML only.
  @JacksonXmlProperty(localName = "INDIVIDUAL_NAME")
  private String individualName;
}
