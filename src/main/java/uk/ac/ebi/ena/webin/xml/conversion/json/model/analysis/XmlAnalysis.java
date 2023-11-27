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
package uk.ac.ebi.ena.webin.xml.conversion.json.model.analysis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.Attribute;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.File;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.Identifiers;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.RefObject;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.xref.Link;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.mapping.FromXmlMapper;

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
  "xmlAnalysisType",
  "files",
  "attributes",
  "links"
})
@Data
@JacksonXmlRootElement(localName = "ANALYSIS")
public class XmlAnalysis implements FromXmlMapper<Analysis> {
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

  @JacksonXmlElementWrapper(localName = "SAMPLE_REF", useWrapping = false)
  @JacksonXmlProperty(localName = "SAMPLE_REF")
  private List<RefObject> samples;

  @JacksonXmlElementWrapper(localName = "EXPERIMENT_REF", useWrapping = false)
  @JacksonXmlProperty(localName = "EXPERIMENT_REF")
  private List<RefObject> experiments;

  @JacksonXmlElementWrapper(localName = "RUN_REF", useWrapping = false)
  @JacksonXmlProperty(localName = "RUN_REF")
  private List<RefObject> runs;

  @JacksonXmlElementWrapper(localName = "ANALYSIS_REF", useWrapping = false)
  @JacksonXmlProperty(localName = "ANALYSIS_REF")
  private List<RefObject> analyses;

  @JacksonXmlProperty(localName = "ANALYSIS_TYPE")
  private XmlAnalysisType xmlAnalysisType;

  @JacksonXmlElementWrapper(localName = "FILES")
  @JacksonXmlProperty(localName = "FILE")
  private List<File> files;

  @JacksonXmlElementWrapper(localName = "ANALYSIS_ATTRIBUTES")
  @JacksonXmlProperty(localName = "ANALYSIS_ATTRIBUTE")
  private List<Attribute> attributes;

  @JacksonXmlElementWrapper(localName = "ANALYSIS_LINKS")
  @JacksonXmlProperty(localName = "ANALYSIS_LINK")
  private List<Link> links;

  @Override
  public Analysis mapFromXml() {
    final Analysis to = new Analysis();

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

    final Map<String, XmlAnalysisType.XmlAnalysisTypeFields> analysisTypeToAnalysisTypeFieldsMap =
        xmlAnalysisType.getAnalysisTypeMap();

    if (analysisTypeToAnalysisTypeFieldsMap != null) {
      analysisTypeToAnalysisTypeFieldsMap.forEach(
          (analysisType, xmlAnalysisTypeFields) -> {
            if (analysisType != null) {
              to.setAnalysisType(analysisType);
            }

            if (xmlAnalysisTypeFields != null) {
              final List<XmlAnalysisType.Assembly> assemblies =
                  xmlAnalysisTypeFields.getAssemblies();

              if (assemblies != null) {
                to.setAssemblies(assemblies);
              }

              if (attributes == null) {
                attributes = new ArrayList<>();
              }

              final List<String> experimentTypes = xmlAnalysisTypeFields.getExperimentTypes();

              if (experimentTypes != null) {
                experimentTypes.forEach(
                    experimentType -> {
                      final Attribute attribute = new Attribute();

                      attribute.setTag("EXPERIMENT_TYPE");
                      attribute.setValue(experimentType);

                      attributes.add(attribute);
                    });
              }

              xmlAnalysisTypeFields
                  .getAnalysisTypeAttributesMap()
                  .forEach(
                      (attributeName, attributeValue) -> {
                        final Attribute attribute = new Attribute();

                        attribute.setTag(attributeName);
                        attribute.setValue(attributeValue);

                        attributes.add(attribute);
                      });
            }
          });
    }

    to.setFiles(files);
    to.setAttributes(attributes);

    if (attributes != null && attributes.size() == 0) {
      attributes = null;
    }

    to.setLinks(links);

    return to;
  }
}
