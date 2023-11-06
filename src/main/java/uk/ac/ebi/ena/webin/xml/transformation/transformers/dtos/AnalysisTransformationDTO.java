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

public class AnalysisTransformationDTO extends PresentationTransformationDTO {
  String analysisId;
  String studyId;
  private Map<String, String> sampleAccessions = Collections.emptyMap();

  public String getAnalysisId() {
    return analysisId;
  }

  public void setAnalysisId(String analysisId) {
    this.analysisId = analysisId;
  }

  public String getStudyId() {
    return studyId;
  }

  public void setStudyId(String studyId) {
    this.studyId = studyId;
  }

  public Map<String, String> getSampleAccessions() {
    return sampleAccessions;
  }

  public void setSampleAccessions(Map<String, String> sampleAccessions) {
    this.sampleAccessions = sampleAccessions;
  }
}
