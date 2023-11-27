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
package uk.ac.ebi.ena.webin.xml.conversion.json.model.analysis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.Attribute;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.File;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.Identifiers;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.RefObject;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.xref.Link;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.mapping.ToXmlMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

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
  "experiments",
  "runs",
  "analysis",
  "analysisType",
  "assemblies",
  "files",
  "attributes",
  "links"
})
@Data
public class Analysis implements ToXmlMapper<XmlAnalysis> {
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

  private List<RefObject> experiments;

  private List<RefObject> runs;

  private List<RefObject> analyses;

  private String analysisType;

  private List<XmlAnalysisType.Assembly> assemblies;

  private List<File> files;

  private List<Attribute> attributes;

  private List<Link> links;

  @Override
  public XmlAnalysis mapToXml() {
    final XmlAnalysis to = new XmlAnalysis();
    final List<Attribute> attributesInACopyOnWriteArrayList =
        new CopyOnWriteArrayList<>(attributes != null ? attributes : new ArrayList<>());

    to.setAlias(alias);
    to.setAccession(accession);
    to.setIdentifiers(identifiers);
    to.setCenterName(centerName);
    to.setBrokerName(brokerName);
    to.setTitle(title);
    to.setDescription(description);
    to.setStudy(study);
    to.setSamples(samples);
    to.setExperiments(experiments);
    to.setRuns(runs);
    to.setAnalyses(analyses);

    final XmlAnalysisType xmlAnalysisType = new XmlAnalysisType();
    final XmlAnalysisType.XmlAnalysisTypeFields xmlAnalysisTypeFields =
        new XmlAnalysisType.XmlAnalysisTypeFields();
    final Map<String, String> analysisTypeElementsMap = new HashMap<>();

    xmlAnalysisTypeFields.setAssemblies(assemblies);

    for (final XmlAnalysisType.AnalysisTypes analysisTypes :
        XmlAnalysisType.AnalysisTypes.values()) {
      if (analysisTypes.name().equalsIgnoreCase(analysisType)) {
        final List<String> analysisTypePreDefinedElementNames =
            XmlAnalysisType.AnalysisTypes.valueOf(analysisType).getAnalysisTypeElements();

        if (analysisTypePreDefinedElementNames.size() > 0) {
          attributesInACopyOnWriteArrayList.forEach(
              attr -> {
                if (analysisTypePreDefinedElementNames.contains(attr.getTag())) {
                  analysisTypeElementsMap.put(attr.getTag(), attr.getValue());
                }

                attributesInACopyOnWriteArrayList.remove(attr);
              });
        }

        break; // an analysis will have only one analysis type and hence it's safe to break here.
      }
    }

    xmlAnalysisTypeFields.setAnalysisTypeAttributesMap(analysisTypeElementsMap);
    xmlAnalysisType.setAnalysisTypeMap(analysisType, xmlAnalysisTypeFields);

    to.setXmlAnalysisType(xmlAnalysisType);
    to.setFiles(files);
    to.setAttributes(attributesInACopyOnWriteArrayList);
    to.setLinks(links);

    return to;
  }
}
