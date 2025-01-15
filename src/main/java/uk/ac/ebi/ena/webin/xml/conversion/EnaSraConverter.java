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
package uk.ac.ebi.ena.webin.xml.conversion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import java.io.InputStream;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.WebinSubmission;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.analysis.Analysis;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.analysis.XmlAnalysis;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.experiment.Experiment;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.experiment.XmlExperiment;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.mixin.IdentifiersJsonMixIn;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.project.Project;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.project.XmlProject;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.receipt.Receipt;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.run.Run;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.run.XmlRun;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.sample.Sample;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.study.Study;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.study.XmlStudy;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.submission.Submission;

/** Converts between ENA JSONs and ENA XMLs. */
public class EnaSraConverter {

  private final ObjectMapper jsonMapper = new ObjectMapper();
  private final XmlMapper xmlMapper = new XmlMapper();

  public EnaSraConverter() {
    setupJsonMapper();
    setupXmlMapper();
  }

  private void setupXmlMapper() {
    xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
    xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
  }

  private void setupJsonMapper() {
    jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    jsonMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    jsonMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    jsonMapper.addMixIn(Sample.class, IdentifiersJsonMixIn.class);
    jsonMapper.addMixIn(Study.class, IdentifiersJsonMixIn.class);
    jsonMapper.addMixIn(Project.class, IdentifiersJsonMixIn.class);
    jsonMapper.addMixIn(Experiment.class, IdentifiersJsonMixIn.class);
  }

  // JSON to XML

  public String convertSubmissionJsonToXml(InputStream json) {
    return writeSubmissionXml(readSubmissionJson(json));
  }

  public String convertSubmissionJsonToXml(String json) {
    return writeSubmissionXml(readSubmissionJson(json));
  }

  public String convertProjectJsonToXml(String json) {
    return writeProjectXml(readProjectJson(json));
  }

  public String convertExperimentJsonToXml(String json) {
    return writeExperimentXml(readExperimentJson(json));
  }

  public String convertRunJsonToXml(String json) {
    return writeRunXml(readRunJson(json));
  }

  public String convertAnalysisJsonToXml(String json) {
    return writeAnalysisXml(readAnalysisJson(json));
  }

  public String convertReceiptJsonToXml(String json) {
    return writeReceiptXml(readReceiptJson(json));
  }

  // XML to JSON

  public String convertSubmissionXmlToJson(String xml) {
    return writeSubmissionJson(readSubmissionXml(xml));
  }

  public String convertProjectXmlToJson(String xml) {
    return writeProjectJson(readProjectXml(xml));
  }

  public String convertExperimentXmlToJson(String xml) {
    return writeExperimentJson(readExperimentXml(xml));
  }

  public String convertRunXmlToJson(String xml) {
    return writeRunJson(readRunXml(xml));
  }

  public String convertAnalysisXmlToJson(String xml) {
    return writeAnalysisJson(readAnalysisXml(xml));
  }

  public String convertReceiptXmlToJson(String xml) {
    return writeReceiptJson(readReceiptXml(xml));
  }

  public String convertSampleXmlToJson(String xml) {
    return writeSampleJson(readSampleXml(xml));
  }

  public String convertStudyXmlToJson(String xml) {
    return writeStudyJson(readStudyXml(xml));
  }

  // From JSON

  public WebinSubmission readSubmissionJson(String json) {
    return readJson(json, WebinSubmission.class, "submission");
  }

  public WebinSubmission readSubmissionJson(InputStream json) {
    return readJson(json, WebinSubmission.class, "submission");
  }

  public Project readProjectJson(String json) {
    return readJson(json, Project.class, "project");
  }

  public Experiment readExperimentJson(String json) {
    return readJson(json, Experiment.class, "experiment");
  }

  public Run readRunJson(String json) {
    return readJson(json, Run.class, "run");
  }

  public Analysis readAnalysisJson(String json) {
    return readJson(json, Analysis.class, "analysis");
  }

  public Receipt readReceiptJson(String json) {
    return readJson(json, Receipt.class, "receipt");
  }

  // From XML

  public Receipt readReceiptXml(String xml) {
    return readXml(xml, Receipt.class, "receipt");
  }

  public Sample readSampleXml(String xml) {
    xml = xml.replace("<SAMPLE_SET>", "").replace("</SAMPLE_SET>", "");
    return readXml(xml, Sample.class, "sample");
  }

  public Submission readSubmissionXml(String xml) {
    xml = xml.replace("<SUBMISSION_SET>", "").replace("</SUBMISSION_SET>", "");
    return readXml(xml, Submission.class, "analysis");
  }

  public Study readStudyXml(String xml) {
    xml = xml.replace("<STUDY_SET>", "").replace("</STUDY_SET>", "");
    return readXml(xml, XmlStudy.class, "study").mapFromXml();
  }

  public Project readProjectXml(String xml) {
    xml = xml.replace("<PROJECT_SET>", "").replace("</PROJECT_SET>", "");
    return readXml(xml, XmlProject.class, "project").mapFromXml();
  }

  public Experiment readExperimentXml(String xml) {
    xml = xml.replace("<EXPERIMENT_SET>", "").replace("</EXPERIMENT_SET>", "");
    return readXml(xml, XmlExperiment.class, "experiment").mapFromXml();
  }

  public Run readRunXml(String xml) {
    xml = xml.replace("<RUN_SET>", "").replace("</RUN_SET>", "");
    return readXml(xml, XmlRun.class, "run").mapFromXml();
  }

  public Analysis readAnalysisXml(String xml) {
    xml = xml.replace("<ANALYSIS_SET>", "").replace("</ANALYSIS_SET>", "");
    return readXml(xml, XmlAnalysis.class, "analysis").mapFromXml();
  }

  // To JSON

  public String writeStudyJson(Study study) {
    return writeJson(study, "study");
  }

  public String writeProjectJson(Project project) {
    return writeJson(project, "project");
  }

  public String writeExperimentJson(Experiment experiment) {
    return writeJson(experiment, "experiment");
  }

  public String writeRunJson(Run run) {
    return writeJson(run, "run");
  }

  public String writeAnalysisJson(Analysis analysis) {
    return writeJson(analysis, "analysis");
  }

  public String writeReceiptJson(Receipt receipt) {
    return writeJson(receipt, "receipt");
  }

  public String writeSampleJson(Sample sample) {
    return writeJson(sample, "sample");
  }

  public String writeSubmissionJson(Submission submission) {
    return writeJson(submission, "submission");
  }

  // To XML

  public String writeSubmissionXml(WebinSubmission webinSubmission) {
    return writeXml(webinSubmission.mapToXml(), "submission");
  }

  public String writeProjectXml(Project project) {
    return writeXml(project.mapToXml(), "project");
  }

  public String writeExperimentXml(Experiment experiment) {
    return writeXml(experiment.mapToXml(), "experiment");
  }

  public String writeRunXml(Run run) {
    return writeXml(run.mapToXml(), "run");
  }

  public String writeAnalysisXml(Analysis analysis) {
    return writeXml(analysis.mapToXml(), "analysis");
  }

  public String writeReceiptXml(Receipt receipt) {
    return writeXml(receipt, "receipt");
  }

  //

  private <T> T readJson(String json, Class<T> cls, String type) {
    try {
      return jsonMapper.readValue(json, cls);
    } catch (Exception ex) {
      throw new WebinXmlConversionException(ex);
    }
  }

  private <T> T readJson(InputStream json, Class<T> cls, String type) {
    try {
      return jsonMapper.readValue(json, cls);
    } catch (Exception ex) {
      throw new WebinXmlConversionException(ex);
    }
  }

  private String writeJson(Object obj, String type) {
    try {
      return jsonMapper.writeValueAsString(obj);
    } catch (Exception ex) {
      throw new WebinXmlConversionException(ex);
    }
  }

  private <T> T readXml(String xml, Class<T> cls, String type) {
    try {
      return xmlMapper.readValue(xml, cls);
    } catch (Exception ex) {
      throw new WebinXmlConversionException(ex);
    }
  }

  private String writeXml(Object obj, String type) {
    try {
      return xmlMapper.writeValueAsString(obj);
    } catch (Exception ex) {
      throw new WebinXmlConversionException(ex);
    }
  }
}
