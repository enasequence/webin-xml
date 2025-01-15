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
package uk.ac.ebi.ena.webin.xml.conversion.model.ena.experiment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import uk.ac.ebi.ena.webin.xml.conversion.utils.LibraryLayoutDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
  "libraryName",
  "libraryStrategy",
  "librarySource",
  "librarySelection",
  "libraryLayout",
  "poolingStrategy",
  "libraryConstructionProtocol"
})
@Data
public class LibraryDescriptor {
  @JacksonXmlProperty(localName = "LIBRARY_NAME")
  private String libraryName;

  @JacksonXmlProperty(localName = "LIBRARY_STRATEGY")
  private String libraryStrategy;

  @JacksonXmlProperty(localName = "LIBRARY_SOURCE")
  private String librarySource;

  @JacksonXmlProperty(localName = "LIBRARY_SELECTION")
  private String librarySelection;

  @JacksonXmlProperty(localName = "LIBRARY_LAYOUT")
  @JsonDeserialize(using = LibraryLayoutDeserializer.class)
  private String libraryLayout;

  @JacksonXmlProperty(localName = "POOLING_STRATEGY")
  private String poolingStrategy;

  @JacksonXmlProperty(localName = "LIBRARY_CONSTRUCTION_PROTOCOL")
  private String libraryConstructionProtocol;
}
