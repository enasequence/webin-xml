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

public class PresentationTransformationDTO {

  private String submissionId;

  private String centerName;
  private String brokerName;

  private String firstPublic;
  private String lastUpdated;

  private Set<String> secondary = Collections.emptySet();

  public String getSubmissionId() {
    return submissionId;
  }

  public void setSubmissionId(String submissionId) {
    this.submissionId = submissionId;
  }

  public String getCenterName() {
    return centerName;
  }

  public void setCenterName(String centerName) {
    this.centerName = centerName;
  }

  public String getBrokerName() {
    return brokerName;
  }

  public void setBrokerName(String brokerName) {
    this.brokerName = brokerName;
  }

  public String getFirstPublic() {
    return firstPublic;
  }

  public void setFirstPublic(String firstPublic) {
    this.firstPublic = firstPublic;
  }

  public String getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(String lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public Set<String> getSecondary() {
    return secondary;
  }

  public void setSecondary(Set<String> secondary) {
    this.secondary = secondary;
  }
}
