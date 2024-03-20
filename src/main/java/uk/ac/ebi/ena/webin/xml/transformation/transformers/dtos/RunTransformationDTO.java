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
import java.util.Set;

public class RunTransformationDTO extends PresentationTransformationDTO {
  private String runId;
  private String studyId;

  private String submissionAlias;
  private String experimentTitle;

  private Set<String> sampleIds = Collections.emptySet();

  public String getRunId() {
    return runId;
  }

  public void setRunId(String runId) {
    this.runId = runId;
  }

  public String getStudyId() {
    return studyId;
  }

  public void setStudyId(String studyId) {
    this.studyId = studyId;
  }

  public Set<String> getSampleIds() {
    return sampleIds;
  }

  public void setSampleIds(Set<String> sampleIds) {
    this.sampleIds = sampleIds;
  }

  public String getSubmissionAlias() {
    return submissionAlias;
  }

  public void setSubmissionAlias(String submissionAlias) {
    this.submissionAlias = submissionAlias;
  }

  public String getExperimentTitle() {
    return experimentTitle;
  }

  public void setExperimentTitle(String experimentTitle) {
    this.experimentTitle = experimentTitle;
  }
}
