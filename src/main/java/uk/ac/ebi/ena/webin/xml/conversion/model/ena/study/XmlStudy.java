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
package uk.ac.ebi.ena.webin.xml.conversion.model.ena.study;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;
import lombok.Data;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.common.Attribute;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.common.Identifiers;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.common.xref.Link;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.mapping.FromXmlMapper;

@JsonPropertyOrder({
  "alias",
  "accession",
  "identifiers",
  "centerName",
  "brokerName",
  "descriptor",
  "attributes",
  "links"
})
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class XmlStudy implements FromXmlMapper<Study> {
  @JsonPropertyOrder({
    "title",
    "studyType",
    "studyAbstract",
    "centerProjectName",
    "studyDescription"
  })
  @Data
  public static class Descriptor {
    @JacksonXmlProperty(localName = "STUDY_TITLE")
    private String title;

    @JacksonXmlProperty(localName = "STUDY_TYPE")
    private StudyType studyType;

    @JacksonXmlProperty(localName = "STUDY_ABSTRACT")
    private String studyAbstract;

    @JacksonXmlProperty(localName = "CENTER_PROJECT_NAME")
    private String centerProjectName;

    @JacksonXmlProperty(localName = "STUDY_DESCRIPTION")
    private String studyDescription;
  }

  @JsonPropertyOrder({"existingStudyType", "newStudyType"})
  @Data
  public static class StudyType {
    @JacksonXmlProperty(localName = "existing_study_type", isAttribute = true)
    private String existingStudyType;

    @JacksonXmlProperty(localName = "new_study_type", isAttribute = true)
    private String newStudyType;
  }

  @JacksonXmlProperty(localName = "alias", isAttribute = true)
  private String alias;

  @JacksonXmlProperty(localName = "accession", isAttribute = true)
  private String accession;

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "IDENTIFIERS")
  private Identifiers identifiers;

  @JacksonXmlProperty(localName = "center_name", isAttribute = true)
  private String centerName;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JacksonXmlProperty(localName = "broker_name", isAttribute = true)
  private String brokerName;

  @JacksonXmlProperty(localName = "DESCRIPTOR")
  private Descriptor descriptor;

  @JacksonXmlElementWrapper(localName = "STUDY_ATTRIBUTES")
  @JacksonXmlProperty(localName = "STUDY_ATTRIBUTE")
  private List<Attribute> attributes;

  @JacksonXmlElementWrapper(localName = "STUDY_LINKS")
  @JacksonXmlProperty(localName = "STUDY_LINK")
  private List<Link> links;

  @Override
  public Study mapFromXml() {
    Study to = new Study();

    to.setAlias(alias);
    to.setAccession(accession);
    to.setIdentifiers(identifiers);
    to.setCenterName(centerName);
    to.setBrokerName(brokerName);
    if (descriptor != null) {
      to.setTitle(descriptor.title);
      // Set study description.
      String studyDescription = descriptor.studyDescription;
      String studyAbstract = descriptor.studyAbstract;
      if (studyDescription != null && studyAbstract == null) {
        to.setDescription(studyDescription);
      } else if (studyDescription == null && studyAbstract != null) {
        to.setDescription(studyAbstract);
      } else if (studyDescription != null) {
        if (studyDescription.length() > studyAbstract.length()) {
          to.setDescription(studyDescription);
        } else {
          to.setDescription(studyAbstract);
        }
      }
    }

    to.setAttributes(attributes);
    to.setLinks(links);
    return to;
  }
}
