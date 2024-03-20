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

public class ProjectTransformationDTO extends PresentationTransformationDTO {

  private String projectId;

  private Set<String> studyIds = Collections.emptySet();
  private Set<String> children = Collections.emptySet();

  public String getProjectId() {
    return projectId;
  }

  public void setProjectId(String projectId) {
    this.projectId = projectId;
  }

  public Set<String> getStudyIds() {
    return studyIds;
  }

  public void setStudyIds(Set<String> studyIds) {
    this.studyIds = studyIds;
  }

  public Set<String> getChildren() {
    return children;
  }

  public void setChildren(Set<String> children) {
    this.children = children;
  }
}
