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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.Attribute;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.File;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.Identifiers;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.RefObject;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.XmlPlatform;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.xref.Link;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.mapping.ToXmlMapper;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
  "alias",
  "accession",
  "identifiers",
  "centerName",
  "brokerName",
  "title",
  "experiment",
  "instrumentPlatform",
  "instrumentModel",
  "files",
  "attributes",
  "links"
})
@Data
public class Run implements ToXmlMapper<XmlRun> {
  private String alias;

  private String accession;

  private Identifiers identifiers;

  private String centerName;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String brokerName;

  private String title;

  private RefObject experiment;

  private String instrumentPlatform;
  private String instrumentModel;

  private List<File> files;

  private List<Attribute> attributes;

  private List<Link> links;

  @Override
  public XmlRun mapToXml() {
    final XmlRun to = new XmlRun();

    to.setAlias(alias);
    to.setAccession(accession);
    to.setIdentifiers(identifiers);
    to.setCenterName(centerName);
    to.setBrokerName(brokerName);
    to.setTitle(title);
    to.setExperiment(experiment);

    if (instrumentPlatform != null && instrumentModel != null) {
      final XmlPlatform xmlPlatform = new XmlPlatform();

      xmlPlatform.setPlatformToInstrumentModelMap(
          instrumentPlatform, Collections.singletonMap("INSTRUMENT_MODEL", instrumentModel));

      to.setPlatform(xmlPlatform);
    }

    final XmlRun.XmlDataBlock xmlDataBlock = new XmlRun.XmlDataBlock();

    xmlDataBlock.setFiles(files);
    to.setDataBlock(xmlDataBlock);

    to.setAttributes(attributes);
    to.setLinks(links);

    return to;
  }
}
