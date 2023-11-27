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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.Attribute;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.Identifiers;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.xref.Link;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.mapping.ToXmlMapper;

import java.util.Collections;
import java.util.List;

@JsonPropertyOrder({
  "alias",
  "accession",
  "identifiers",
  "centerName",
  "brokerName",
  "name",
  "title",
  "description",
  "sequencingProject",
  "adminProject",
  "relatedProjects",
  "attributes",
  "links"
})
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Project implements ToXmlMapper<XmlProject> {
  private String alias;
  private String accession;
  private Identifiers identifiers;
  private String centerName;
  private String brokerName;
  private String name;
  private String title;
  private String description;
  private SequencingProject sequencingProject;
  private AdminProject adminProject;

  @JsonProperty("relationships")
  private List<RelatedProject> relatedProjects;

  private List<Attribute> attributes;
  private List<Link> links;

  @Override
  public XmlProject mapToXml() {
    final XmlProject to = new XmlProject();

    to.setAlias(alias);
    to.setAccession(accession);
    to.setIdentifiers(identifiers);
    to.setCenterName(centerName);
    to.setBrokerName(brokerName);
    to.setName(name);
    to.setTitle(title);
    to.setDescription(description);

    if (sequencingProject != null && sequencingProject.getLocusTagPrefixes() == null) {
      sequencingProject.setLocusTagPrefixes(Collections.emptyList());
    }

    to.setSequencingProject(sequencingProject);
    to.setAdminProject(adminProject);
    to.setRelatedProjects(relatedProjects);
    to.setAttributes(attributes);
    to.setLinks(links);

    return to;
  }
}
