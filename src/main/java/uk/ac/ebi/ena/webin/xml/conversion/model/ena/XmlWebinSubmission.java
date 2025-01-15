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
package uk.ac.ebi.ena.webin.xml.conversion.model.ena;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;
import java.util.stream.Collectors;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.analysis.XmlAnalysis;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.experiment.XmlExperiment;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.mapping.FromXmlMapper;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.project.XmlProject;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.run.XmlRun;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.sample.Sample;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.study.XmlStudy;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.submission.Submission;

@JsonPropertyOrder({"submission", "samples", "studies", "experiments"})
@JacksonXmlRootElement(localName = "WEBIN")
public class XmlWebinSubmission implements FromXmlMapper<WebinSubmission> {
  @JacksonXmlProperty(localName = "SUBMISSION")
  private Submission submission;

  @JacksonXmlElementWrapper(localName = "SAMPLE_SET")
  @JacksonXmlProperty(localName = "SAMPLE")
  private List<Sample> samples;

  @JacksonXmlElementWrapper(localName = "STUDY_SET")
  @JacksonXmlProperty(localName = "STUDY")
  private List<XmlStudy> studies;

  @JacksonXmlElementWrapper(localName = "PROJECT_SET")
  @JacksonXmlProperty(localName = "PROJECT")
  private List<XmlProject> projects;

  @JacksonXmlElementWrapper(localName = "EXPERIMENT_SET")
  @JacksonXmlProperty(localName = "EXPERIMENT")
  private List<XmlExperiment> experiments;

  @JacksonXmlElementWrapper(localName = "RUN_SET")
  @JacksonXmlProperty(localName = "RUN")
  private List<XmlRun> runs;

  @JacksonXmlElementWrapper(localName = "ANALYSIS_SET")
  @JacksonXmlProperty(localName = "ANALYSIS")
  private List<XmlAnalysis> analyses;

  public Submission getSubmission() {
    return submission;
  }

  public void setSubmission(Submission submission) {
    this.submission = submission;
  }

  public List<Sample> getSamples() {
    return samples;
  }

  public void setSamples(List<Sample> samples) {
    this.samples = samples;
  }

  public List<XmlStudy> getStudies() {
    return studies;
  }

  public void setStudies(List<XmlStudy> studies) {
    this.studies = studies;
  }

  public List<XmlProject> getProjects() {
    return projects;
  }

  public void setProjects(List<XmlProject> projects) {
    this.projects = projects;
  }

  public List<XmlExperiment> getExperiments() {
    return experiments;
  }

  public void setExperiments(List<XmlExperiment> experiments) {
    this.experiments = experiments;
  }

  public List<XmlRun> getRuns() {
    return runs;
  }

  public void setRuns(List<XmlRun> runs) {
    this.runs = runs;
  }

  public List<XmlAnalysis> getAnalyses() {
    return analyses;
  }

  public void setAnalyses(List<XmlAnalysis> analyses) {
    this.analyses = analyses;
  }

  @Override
  public WebinSubmission mapFromXml() {
    WebinSubmission to = new WebinSubmission();
    to.setSubmission(submission);
    to.setSamples(samples);

    if (studies != null) {
      to.setStudies(studies.stream().map(XmlStudy::mapFromXml).collect(Collectors.toList()));
    }

    if (projects != null) {
      to.setProjects(projects.stream().map(XmlProject::mapFromXml).collect(Collectors.toList()));
    }

    if (experiments != null) {
      to.setExperiments(
          experiments.stream().map(XmlExperiment::mapFromXml).collect(Collectors.toList()));
    }

    if (runs != null) {
      to.setRuns(runs.stream().map(XmlRun::mapFromXml).collect(Collectors.toList()));
    }

    if (analyses != null) {
      to.setAnalyses(analyses.stream().map(XmlAnalysis::mapFromXml).collect(Collectors.toList()));
    }

    return to;
  }
}
