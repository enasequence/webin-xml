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
package uk.ac.ebi.ena.webin.xml.conversion.model.ena.experiment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.common.Attribute;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.common.Identifiers;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.common.RefObject;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.common.XmlPlatform;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.common.xref.Link;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.mapping.ToXmlMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
  "alias",
  "accession",
  "identifiers",
  "centerName",
  "brokerName",
  "title",
  "description",
  "study",
  "samples",
  "designDescription",
  "libraryDescriptor",
  "instrumentPlatform",
  "instrumentModel",
  "attributes",
  "links"
})
@Data
public class Experiment implements ToXmlMapper<XmlExperiment> {
  private String alias;

  private String accession;

  private Identifiers identifiers;

  private String centerName;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String brokerName;

  private String title;

  private String description;

  private RefObject study;
  private List<RefObject> samples;

  private String designDescription;
  private LibraryDescriptor libraryDescriptor;

  private String instrumentPlatform;
  private String instrumentModel;

  private List<Attribute> attributes;

  private List<Link> links;

  @Override
  public XmlExperiment mapToXml() {
    final XmlExperiment to = new XmlExperiment();

    to.setAlias(alias);
    to.setAccession(accession);
    to.setIdentifiers(identifiers);
    to.setCenterName(centerName);
    to.setBrokerName(brokerName);
    to.setTitle(title);
    to.setDescription(description);
    to.setStudy(study);

    final XmlExperiment.XmlDesign xmlDesign = new XmlExperiment.XmlDesign();

    xmlDesign.setSamples(samples);
    xmlDesign.setDesignDescription(designDescription != null ? designDescription : "");
    xmlDesign.setLibraryDescriptor(libraryDescriptor);

    to.setDesign(xmlDesign);

    final XmlPlatform xmlPlatform = new XmlPlatform();

    xmlPlatform.setPlatformToInstrumentModelMap(
        instrumentPlatform, Collections.singletonMap("INSTRUMENT_MODEL", instrumentModel));

    to.setPlatform(xmlPlatform);

    to.setAttributes(attributes);
    to.setLinks(links);

    return to;
  }
}
