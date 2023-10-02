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
package uk.ac.ebi.ena.webin.xml.transformation.template;

import java.io.File;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import uk.ac.ebi.ena.webin.xml.transformation.WebinXmlTransformationException;

public class ClasspathLoadedTemplateProvider implements TemplateProvider {

  private final String studyTemplateClasspathUrl;
  private final String projectTemplateClasspathUrl;
  private final String sampleTemplateClasspathUrl;
  private final String experimentTemplateClasspathUrl;
  private final String runTemplateClasspathUrl;
  private final String analysisTemplateClasspathUrl;
  private final String egaDacTemplateClasspathUrl;
  private final String egaPolicyTemplateClasspathUrl;
  private final String egaDatasetTemplateClasspathUrl;
  private final String submissionTemplateClasspathUrl;

  public ClasspathLoadedTemplateProvider(
      String studyTemplateClasspathUrl,
      String projectTemplateClasspathUrl,
      String sampleTemplateClasspathUrl,
      String experimentTemplateClasspathUrl,
      String runTemplateClasspathUrl,
      String analysisTemplateClasspathUrl,
      String egaDacTemplateClasspathUrl,
      String egaPolicyTemplateClasspathUrl,
      String egaDatasetTemplateClasspathUrl,
      String submissionTemplateClasspathUrl) {

    this.studyTemplateClasspathUrl = studyTemplateClasspathUrl;
    this.projectTemplateClasspathUrl = projectTemplateClasspathUrl;
    this.sampleTemplateClasspathUrl = sampleTemplateClasspathUrl;
    this.experimentTemplateClasspathUrl = experimentTemplateClasspathUrl;
    this.runTemplateClasspathUrl = runTemplateClasspathUrl;
    this.analysisTemplateClasspathUrl = analysisTemplateClasspathUrl;
    this.egaDacTemplateClasspathUrl = egaDacTemplateClasspathUrl;
    this.egaPolicyTemplateClasspathUrl = egaPolicyTemplateClasspathUrl;
    this.egaDatasetTemplateClasspathUrl = egaDatasetTemplateClasspathUrl;
    this.submissionTemplateClasspathUrl = submissionTemplateClasspathUrl;
  }

  @Override
  public Templates getStudyTemplate() throws WebinXmlTransformationException {
    return loadTransformationTemplateFromClasspath(studyTemplateClasspathUrl);
  }

  @Override
  public Templates getProjectTemplate() throws WebinXmlTransformationException {
    return loadTransformationTemplateFromClasspath(projectTemplateClasspathUrl);
  }

  @Override
  public Templates getSampleTemplate() throws WebinXmlTransformationException {
    return loadTransformationTemplateFromClasspath(sampleTemplateClasspathUrl);
  }

  @Override
  public Templates getExperimentTemplate() throws WebinXmlTransformationException {
    return loadTransformationTemplateFromClasspath(experimentTemplateClasspathUrl);
  }

  @Override
  public Templates getRunTemplate() throws WebinXmlTransformationException {
    return loadTransformationTemplateFromClasspath(runTemplateClasspathUrl);
  }

  @Override
  public Templates getAnalysisTemplate() throws WebinXmlTransformationException {
    return loadTransformationTemplateFromClasspath(analysisTemplateClasspathUrl);
  }

  @Override
  public Templates getEGADacTemplate() throws WebinXmlTransformationException {
    return loadTransformationTemplateFromClasspath(egaDacTemplateClasspathUrl);
  }

  @Override
  public Templates getEGAPolicyTemplate() throws WebinXmlTransformationException {
    return loadTransformationTemplateFromClasspath(egaPolicyTemplateClasspathUrl);
  }

  @Override
  public Templates getEGADatasetTemplate() throws WebinXmlTransformationException {
    return loadTransformationTemplateFromClasspath(egaDatasetTemplateClasspathUrl);
  }

  @Override
  public Templates getSubmissionTemplate() throws WebinXmlTransformationException {
    return loadTransformationTemplateFromClasspath(submissionTemplateClasspathUrl);
  }

  private Templates loadTransformationTemplateFromClasspath(String path)
      throws WebinXmlTransformationException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    transformerFactory.setURIResolver(
        (String href, String base) -> {
          String local = new File(path).getParent().replaceAll("[\\\\]+", "/");

          return href.startsWith("/")
              ? new StreamSource(ClasspathLoadedTemplateProvider.class.getResourceAsStream(href))
              : new StreamSource(
                  ClasspathLoadedTemplateProvider.class.getResourceAsStream(local + "/" + href));
        });

    try {
      return transformerFactory.newTemplates(
          new StreamSource(ClasspathLoadedTemplateProvider.class.getResourceAsStream(path)));
    } catch (TransformerConfigurationException e) {
      throw new WebinXmlTransformationException(
          "Error loading transformation template from classpath : " + path, e);
    }
  }
}
