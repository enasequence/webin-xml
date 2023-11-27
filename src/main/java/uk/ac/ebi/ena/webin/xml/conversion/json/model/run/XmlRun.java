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
package uk.ac.ebi.ena.webin.xml.conversion.json.model.run;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.Attribute;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.File;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.Identifiers;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.RefObject;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.XmlPlatform;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.xref.Link;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.mapping.FromXmlMapper;

import java.util.List;
import java.util.Map;

@JacksonXmlRootElement(localName = "RUN")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
  "alias",
  "accession",
  "identifiers",
  "centerName",
  "brokerName",
  "title",
  "experiment",
  "platform",
  "dataBlock",
  "attributes",
  "links"
})
@Data
public class XmlRun implements FromXmlMapper<Run> {
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

  @JacksonXmlProperty(localName = "EXPERIMENT_REF")
  private RefObject experiment;

  @JacksonXmlProperty(localName = "PLATFORM")
  private XmlPlatform platform;

  @JacksonXmlProperty(localName = "DATA_BLOCK")
  private XmlDataBlock dataBlock;

  @JacksonXmlElementWrapper(localName = "RUN_ATTRIBUTES")
  @JacksonXmlProperty(localName = "RUN_ATTRIBUTE")
  private List<Attribute> attributes;

  @JacksonXmlElementWrapper(localName = "RUN_LINKS")
  @JacksonXmlProperty(localName = "RUN_LINK")
  private List<Link> links;

  @Data
  public static class XmlDataBlock {
    @JacksonXmlElementWrapper(localName = "FILES")
    @JacksonXmlProperty(localName = "FILE")
    private List<File> files;
  }

  @Override
  public Run mapFromXml() {
    final Run to = new Run();

    to.setAlias(alias);
    to.setAccession(accession);
    to.setIdentifiers(identifiers);
    to.setCenterName(centerName);
    to.setBrokerName(brokerName);
    to.setTitle(title);

    to.setExperiment(experiment);

    if (platform != null) {
      final Map<String, Map<String, String>> instrumentMap =
          platform.getPlatformToInstrumentModelMap();
      final Map.Entry<String, Map<String, String>> instrumentEntry =
          instrumentMap.entrySet().iterator().next();

      to.setInstrumentPlatform(instrumentEntry.getKey());
      to.setInstrumentModel(instrumentEntry.getValue().entrySet().iterator().next().getValue());
    }

    to.setFiles(dataBlock.getFiles());
    to.setAttributes(attributes);
    to.setLinks(links);

    return to;
  }
}
