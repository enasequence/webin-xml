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
package uk.ac.ebi.ena.webin.xml.conversion.json.model.analysis;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.RefObject;

@Data
public class XmlAnalysisType {
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private final Map<String, XmlAnalysisTypeFields> analysisTypeToAnalysisTypeFieldsMap =
      new HashMap<>();

  @JsonAnyGetter
  public Map<String, XmlAnalysisTypeFields> getAnalysisTypeMap() {
    return analysisTypeToAnalysisTypeFieldsMap;
  }

  @JsonAnySetter
  public void setAnalysisTypeMap(final String name, final XmlAnalysisTypeFields value) {
    this.analysisTypeToAnalysisTypeFieldsMap.put(name, value);
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPropertyOrder({"assemblies", "analysisTypeAttributesMap"})
  @Data
  public static class XmlAnalysisTypeFields {
    @JacksonXmlElementWrapper(localName = "ASSEMBLY", useWrapping = false)
    @JacksonXmlProperty(localName = "ASSEMBLY")
    private List<Assembly> assemblies;

    @JacksonXmlElementWrapper(localName = "EXPERIMENT_TYPE", useWrapping = false)
    @JacksonXmlProperty(localName = "EXPERIMENT_TYPE")
    private List<String> experimentTypes;

    private Map<String, String> analysisTypeAttributesMap = new HashMap<>();

    @JsonAnyGetter
    public Map<String, String> getAnalysisTypeAttributesMap() {
      return analysisTypeAttributesMap;
    }

    @JsonAnySetter
    public void setAnalysisTypeAttributesMap(final String name, final String value) {
      this.analysisTypeAttributesMap.put(name, value);
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class Assembly {
    @JacksonXmlProperty(localName = "STANDARD")
    private RefObject assembly;
  }

  public enum AnalysisTypes {
    GENOME_MAP("PROGRAM", "PLATFORM", "DESCRIPTION"),
    SEQUENCE_VARIATION("PROGRAM", "PLATFORM", "IMPUTATION", "EXPERIMENT_TYPE"),
    SEQUENCE_ASSEMBLY(
        "NAME",
        "TYPE",
        "PARTIAL",
        "COVERAGE",
        "PROGRAM",
        "PLATFORM",
        "MIN_GAP_LENGTH",
        "MOL_TYPE",
        "TPA",
        "AUTHORS",
        "ADDRESS"),
    SEQUENCE_FLATFILE("AUTHORS", "ADDRESS"),
    TRANSCRIPTOME_ASSEMBLY("NAME", "PROGRAM", "PLATFORM", "TPA", "AUTHORS", "ADDRESS", "TYPE");

    private final List<String> analysisTypeElements;

    AnalysisTypes(final String... analysisTypeElementNames) {
      analysisTypeElements = Arrays.asList(analysisTypeElementNames);
    }

    public List<String> getAnalysisTypeElements() {
      return analysisTypeElements;
    }
  }
}
