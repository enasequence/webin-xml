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
package uk.ac.ebi.ena.webin.xml.conversion.json.model.study;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.Attribute;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.Identifiers;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.xref.Link;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.mapping.ToXmlMapper;

import java.util.List;

@JsonPropertyOrder({
  "alias",
  "accession",
  "identifiers",
  "centerName",
  "brokerName",
  "title",
  "description",
  "attributes",
  "links"
})
@Data
public class Study implements ToXmlMapper<XmlStudy> {
  private String alias;

  private String accession;

  private Identifiers identifiers;

  private String centerName;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String brokerName;

  private String title;

  private String description;

  private List<Attribute> attributes;

  private List<Link> links;

  @Override
  public XmlStudy mapToXml() {
    XmlStudy to = new XmlStudy();

    to.setAlias(alias);
    to.setAccession(accession);
    to.setIdentifiers(identifiers);
    to.setCenterName(centerName);
    to.setBrokerName(brokerName);
    XmlStudy.Descriptor descriptor = new XmlStudy.Descriptor();
    to.setDescriptor(descriptor);
    descriptor.setTitle(title);

    // Set study type to 'Other'.
    XmlStudy.StudyType studyType = new XmlStudy.StudyType();
    descriptor.setStudyType(studyType);
    studyType.setExistingStudyType("Other");

    // Set study description but not study abstract.
    descriptor.setStudyDescription(description);

    to.setAttributes(attributes);
    to.setLinks(links);
    return to;
  }
}
