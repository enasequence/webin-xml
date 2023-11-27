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
package uk.ac.ebi.ena.webin.xml.conversion.json.model.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"childProject", "parentProject"})
@Data
public class RelatedProject {
  @JacksonXmlProperty(localName = "CHILD_PROJECT")
  private RelatedProjectId childProject;

  @JacksonXmlProperty(localName = "PARENT_PROJECT")
  private RelatedProjectId parentProject;

  @Data
  public static class RelatedProjectId {
    @JacksonXmlProperty(localName = "accession", isAttribute = true)
    private String accession;
  }
}
