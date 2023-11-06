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

public class SampleTransformationDTO extends PresentationTransformationDTO {
  private String submissionAlias;
  private String sampleId;
  private String bioSampleId;

  public String getSubmissionAlias() {
    return submissionAlias;
  }

  public void setSubmissionAlias(String submissionAlias) {
    this.submissionAlias = submissionAlias;
  }

  public String getSampleId() {
    return sampleId;
  }

  public void setSampleId(String sampleId) {
    this.sampleId = sampleId;
  }

  public String getBioSampleId() {
    return bioSampleId;
  }

  public void setBioSampleId(String bioSampleId) {
    this.bioSampleId = bioSampleId;
  }
}
