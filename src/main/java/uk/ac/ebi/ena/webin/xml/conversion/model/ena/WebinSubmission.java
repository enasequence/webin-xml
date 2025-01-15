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
import java.util.List;
import java.util.stream.Collectors;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.analysis.Analysis;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.experiment.Experiment;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.mapping.ToXmlMapper;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.project.Project;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.run.Run;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.sample.Sample;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.study.Study;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.submission.Submission;

@JsonPropertyOrder({"submission", "samples", "studies", "projects", "experiments"})
public class WebinSubmission implements ToXmlMapper<XmlWebinSubmission> {
  private Submission submission;

  private List<Sample> samples;

  private List<Study> studies;

  private List<Project> projects;

  private List<Experiment> experiments;

  private List<Run> runs;

  private List<Analysis> analyses;

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

  public List<Study> getStudies() {
    return studies;
  }

  public void setStudies(List<Study> studies) {
    this.studies = studies;
  }

  public List<Project> getProjects() {
    return projects;
  }

  public void setProjects(List<Project> projects) {
    this.projects = projects;
  }

  public List<Experiment> getExperiments() {
    return experiments;
  }

  public void setExperiments(List<Experiment> experiments) {
    this.experiments = experiments;
  }

  public List<Run> getRuns() {
    return runs;
  }

  public void setRuns(List<Run> runs) {
    this.runs = runs;
  }

  public List<Analysis> getAnalyses() {
    return analyses;
  }

  public void setAnalyses(List<Analysis> analyses) {
    this.analyses = analyses;
  }

  @Override
  public XmlWebinSubmission mapToXml() {
    XmlWebinSubmission to = new XmlWebinSubmission();
    to.setSubmission(submission);
    to.setSamples(samples);
    if (studies != null) {
      to.setStudies(studies.stream().map(Study::mapToXml).collect(Collectors.toList()));
    }

    if (projects != null) {
      to.setProjects(projects.stream().map(Project::mapToXml).collect(Collectors.toList()));
    }

    if (experiments != null) {
      to.setExperiments(
          experiments.stream().map(Experiment::mapToXml).collect(Collectors.toList()));
    }

    if (runs != null) {
      to.setRuns(runs.stream().map(Run::mapToXml).collect(Collectors.toList()));
    }

    if (analyses != null) {
      to.setAnalyses(analyses.stream().map(Analysis::mapToXml).collect(Collectors.toList()));
    }

    return to;
  }
}
