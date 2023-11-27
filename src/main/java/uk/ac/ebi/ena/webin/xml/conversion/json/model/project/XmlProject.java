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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.Attribute;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.Identifiers;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.xref.Link;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.mapping.FromXmlMapper;

import java.util.List;

@JacksonXmlRootElement(localName = "PROJECT")
@JsonIgnoreProperties(ignoreUnknown = true)
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
@Data
public class XmlProject implements FromXmlMapper<Project> {
  @JacksonXmlProperty(localName = "alias", isAttribute = true)
  private String alias;

  @JacksonXmlProperty(localName = "accession", isAttribute = true)
  private String accession;

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "IDENTIFIERS")
  private Identifiers identifiers;

  @JacksonXmlProperty(localName = "center_name", isAttribute = true)
  private String centerName;

  @JacksonXmlProperty(localName = "broker_name", isAttribute = true)
  private String brokerName;

  @JacksonXmlProperty(localName = "NAME")
  private String name;

  @JacksonXmlProperty(localName = "TITLE")
  private String title;

  @JacksonXmlProperty(localName = "DESCRIPTION")
  private String description;

  @JacksonXmlProperty(localName = "SUBMISSION_PROJECT")
  private SequencingProject sequencingProject;

  @JacksonXmlProperty(localName = "UMBRELLA_PROJECT")
  private AdminProject adminProject;

  @JacksonXmlElementWrapper(localName = "RELATED_PROJECTS")
  @JacksonXmlProperty(localName = "RELATED_PROJECT")
  private List<RelatedProject> relatedProjects;

  @JacksonXmlElementWrapper(localName = "PROJECT_ATTRIBUTES")
  @JacksonXmlProperty(localName = "PROJECT_ATTRIBUTE")
  private List<Attribute> attributes;

  @JacksonXmlElementWrapper(localName = "PROJECT_LINKS")
  @JacksonXmlProperty(localName = "PROJECT_LINK")
  private List<Link> links;

  @Override
  public Project mapFromXml() {
    final Project to = new Project();

    to.setAlias(alias);
    to.setAccession(accession);
    to.setIdentifiers(identifiers);
    to.setCenterName(centerName);
    to.setBrokerName(brokerName);
    to.setName(name);
    to.setTitle(title);
    to.setDescription(description);
    to.setSequencingProject(sequencingProject);
    to.setAdminProject(adminProject);
    to.setRelatedProjects(relatedProjects);
    to.setAttributes(attributes);
    to.setLinks(links);

    return to;
  }
}
