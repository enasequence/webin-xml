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
package uk.ac.ebi.ena.webin.xml.conversion.json.model.experiment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;
import java.util.Map;
import lombok.Data;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.Attribute;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.Identifiers;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.RefObject;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.XmlPlatform;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.xref.Link;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.mapping.FromXmlMapper;
import uk.ac.ebi.ena.webin.xml.conversion.json.utils.XmlDesignSerializer;

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
  "design",
  "platform",
  "attributes",
  "links"
})
@JacksonXmlRootElement(localName = "EXPERIMENT")
@Data
public class XmlExperiment implements FromXmlMapper<Experiment> {
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

  @JacksonXmlProperty(localName = "TITLE")
  private String title;

  @JacksonXmlProperty(localName = "DESCRIPTION")
  private String description;

  @JacksonXmlProperty(localName = "STUDY_REF")
  private RefObject study;

  @JacksonXmlProperty(localName = "DESIGN")
  @JsonSerialize(using = XmlDesignSerializer.class)
  private XmlDesign design;

  @JacksonXmlProperty(localName = "PLATFORM")
  private XmlPlatform platform;

  @JacksonXmlElementWrapper(localName = "EXPERIMENT_ATTRIBUTES")
  @JacksonXmlProperty(localName = "EXPERIMENT_ATTRIBUTE")
  private List<Attribute> attributes;

  @JacksonXmlElementWrapper(localName = "EXPERIMENT_LINKS")
  @JacksonXmlProperty(localName = "EXPERIMENT_LINK")
  private List<Link> links;

  @JsonPropertyOrder({"designDescription", "samples", "libraryDescriptor"})
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class XmlDesign {
    @JacksonXmlProperty(localName = "DESIGN_DESCRIPTION")
    private String designDescription;

    @JacksonXmlElementWrapper(localName = "SAMPLE_DESCRIPTOR", useWrapping = false)
    @JacksonXmlProperty(localName = "SAMPLE_DESCRIPTOR")
    private List<RefObject> samples;

    @JacksonXmlProperty(localName = "LIBRARY_DESCRIPTOR")
    private LibraryDescriptor libraryDescriptor;
  }

  @Override
  public Experiment mapFromXml() {
    final Experiment to = new Experiment();

    to.setAlias(alias);
    to.setAccession(accession);
    to.setIdentifiers(identifiers);
    to.setCenterName(centerName);
    to.setBrokerName(brokerName);
    to.setTitle(title);
    to.setDescription(description);
    to.setStudy(study);
    to.setSamples(design.getSamples());

    final String designDescription = design.getDesignDescription();

    to.setDesignDescription(designDescription != null ? designDescription : "");
    to.setLibraryDescriptor(design.getLibraryDescriptor());

    final Map<String, Map<String, String>> instrumentMap =
        getPlatform().getPlatformToInstrumentModelMap();
    final Map.Entry<String, Map<String, String>> instrumentEntry =
        instrumentMap.entrySet().iterator().next();

    to.setInstrumentPlatform(instrumentEntry.getKey());
    to.setInstrumentModel(instrumentEntry.getValue().entrySet().iterator().next().getValue());

    to.setAttributes(attributes);
    to.setLinks(links);

    return to;
  }
}
