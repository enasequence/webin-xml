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
package uk.ac.ebi.ena.webin.xml.transformation.transformers.dtos;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ExperimentTransformationDTO extends PresentationTransformationDTO {

  private String experimentId;
  private String instrumentModel;
  private String studyAlias;

  private Set<String> samples = Collections.emptySet();

  private Map<String, String> sampleAccessions = Collections.emptyMap();

  public String getExperimentId() {
    return experimentId;
  }

  public void setExperimentId(String experimentId) {
    this.experimentId = experimentId;
  }

  public String getInstrumentModel() {
    return instrumentModel;
  }

  public void setInstrumentModel(String instrumentModel) {
    this.instrumentModel = instrumentModel;
  }

  public String getStudyAlias() {
    return studyAlias;
  }

  public void setStudyAlias(String studyAlias) {
    this.studyAlias = studyAlias;
  }

  public Set<String> getSamples() {
    return samples;
  }

  public void setSamples(Set<String> samples) {
    this.samples = samples;
  }

  public Map<String, String> getSampleAccessions() {
    return sampleAccessions;
  }

  public void setSampleAccessions(Map<String, String> sampleAccessions) {
    this.sampleAccessions = sampleAccessions;
  }
}
