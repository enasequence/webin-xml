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
package uk.ac.ebi.ena.webin.xml.transformation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.IOUtils;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.Assert;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.*;
import uk.ac.ebi.ena.sra.xml.ANALYSISSETDocument;
import uk.ac.ebi.ena.sra.xml.EXPERIMENTSETDocument;
import uk.ac.ebi.ena.sra.xml.PROJECTSETDocument;
import uk.ac.ebi.ena.sra.xml.RUNSETDocument;
import uk.ac.ebi.ena.sra.xml.SAMPLESETDocument;
import uk.ac.ebi.ena.sra.xml.STUDYSETDocument;
import uk.ac.ebi.ena.sra.xml.SUBMISSIONSETDocument;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.Transformer;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.dtos.AnalysisTransformationDTO;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.dtos.ExperimentTransformationDTO;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.dtos.ProjectTransformationDTO;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.dtos.RunTransformationDTO;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.dtos.SampleTransformationDTO;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.dtos.StudyTransformationDTO;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.dtos.SubmissionTransformationDTO;

public class WebinXmlTransformationTest {

  private static final String BASE_RESOURCE_PATH = "/uk/ac/ebi/ena/webin/xml/transformation/";
  private static final String INPUT_FILE_SUFFIX = ".xml";
  private static final String EXPECTED_FILE_SUFFIX = "-expected.xml";
  private static final DocumentBuilder DOCUMENT_BUILDER = createDocumentBuilder();
  private static final int MAX_XML_DIFFERENCE_COUNT = 5;

  @Test
  public void testStudyXsltTransformation() throws Exception {
    testForDir("study/xslt", "STUDY", "STUDY_SET", WebinXmlTransformation.createStudyTransformer());
  }

  @Test
  public void testStudyPresentationTransformation() throws Exception {
    testPresentationTransformationForFile(
        "study/study",
        xmlInputStream -> {
          STUDYSETDocument doc = STUDYSETDocument.Factory.parse(xmlInputStream);

          StudyTransformationDTO dto = new StudyTransformationDTO();
          dto.setStudyId("SRP301193");
          dto.setProjectId("PRJNA685328");
          dto.setSubmissionId("SRA1173157");
          dto.setCenterName("BioProject");
          dto.setStatusId(4);
          dto.setFirstPublic("2021-01-12");
          dto.setLastUpdated("2021-01-12");

          return WebinXmlTransformation.createStudyTransformer().transformForPresentation(dto, doc);
        });

    testPresentationTransformationForFile(
        "study/study_submitter_namespace_expansion",
        xmlInputStream -> {
          STUDYSETDocument doc = STUDYSETDocument.Factory.parse(xmlInputStream);

          StudyTransformationDTO dto = new StudyTransformationDTO();
          dto.setStudyId("ERP006120");
          dto.setProjectId("PRJEB6572");
          dto.setSubmissionId("ERA319850");
          dto.setCenterName("Wellcome Sanger Institute");
          dto.setStatusId(4);
          dto.setFirstPublic("2014-11-26");
          dto.setLastUpdated("2014-06-20");

          return WebinXmlTransformation.createStudyTransformer().transformForPresentation(dto, doc);
        });
  }

  @Test
  public void testProjectXsltTransformation()
      throws IOException, SAXException, TransformerException {
    testForDir(
        "project/xslt",
        "PROJECT",
        "PROJECT_SET",
        WebinXmlTransformation.createProjectTransformer());
  }

  @Test
  public void testProjectPresentationTransformation() throws Exception {
    testPresentationTransformationForFile(
        "project/project",
        xmlInputStream -> {
          PROJECTSETDocument doc = PROJECTSETDocument.Factory.parse(xmlInputStream);

          ProjectTransformationDTO dto = new ProjectTransformationDTO();
          dto.setProjectId("PRJEB42418");
          dto.setSubmissionId("ERA3206924");
          dto.setCenterName("EMG");
          dto.setBrokerName("EMG broker account, EMBL-EBI");
          dto.setStatusId(4);
          dto.setFirstPublic("2021-01-11");
          dto.setLastUpdated("2021-01-11");
          dto.setStudyIds(Collections.singleton("ERP126272"));

          return WebinXmlTransformation.createProjectTransformer()
              .transformForPresentation(dto, doc);
        });

    testPresentationTransformationForFile(
        "project/project_submitter_namespace_expansion",
        xmlInputStream -> {
          PROJECTSETDocument doc = PROJECTSETDocument.Factory.parse(xmlInputStream);

          ProjectTransformationDTO dto = new ProjectTransformationDTO();
          dto.setProjectId("PRJEB11481");
          dto.setSubmissionId("ERA526107");
          dto.setCenterName("Wellcome Sanger Institute");
          dto.setStatusId(4);
          dto.setFirstPublic("2016-04-19");
          dto.setLastUpdated("2016-05-20");
          dto.setStudyIds(Collections.singleton("ERP012875"));

          return WebinXmlTransformation.createProjectTransformer()
              .transformForPresentation(dto, doc);
        });

    testPresentationTransformationForFile(
        "project/related_projects",
        xmlInputStream -> {
          PROJECTSETDocument doc = PROJECTSETDocument.Factory.parse(xmlInputStream);

          Set<String> parents = new LinkedHashSet<>();
          parents.add("PRJEB40665");
          parents.add("PRJNA489243");

          Set<String> children = new LinkedHashSet<>();
          children.add("PRJEB42168");
          children.add("PRJEB42169");
          children.add("PRJEB42234");

          ProjectTransformationDTO dto = new ProjectTransformationDTO();
          dto.setProjectId("PRJEB42238");
          dto.setSubmissionId("ERA3203394");
          dto.setCenterName("WELLCOME SANGER INSTITUTE");
          dto.setStatusId(4);
          dto.setFirstPublic("2020-12-22");
          dto.setLastUpdated("2021-01-08");
          dto.setParents(parents);
          dto.setChildren(children);

          return WebinXmlTransformation.createProjectTransformer()
              .transformForPresentation(dto, doc);
        });
  }

  @Test
  public void testSampleXsltTransformation()
      throws IOException, SAXException, TransformerException {
    testForDir(
        "sample/xslt", "SAMPLE", "SAMPLE_SET", WebinXmlTransformation.createSampleTransformer());
  }

  @Test
  public void testSamplePresentationTransformation() throws Exception {
    testPresentationTransformationForFile(
        "sample/sample01",
        xmlInputStream -> {
          SAMPLESETDocument doc = SAMPLESETDocument.Factory.parse(xmlInputStream);

          SampleTransformationDTO dto = new SampleTransformationDTO();
          dto.setSampleId("ERS5524409");
          dto.setBioSampleId("SAMEA7777172");
          dto.setSubmissionId("ERA3206930");
          dto.setSubmissionAlias(
              "ena-SUBMISSION-Graz University of Technical-11-01-2021-14:11:54:887-1");
          dto.setCenterName("Graz University of Technical");
          dto.setStatusId(4);
          dto.setFirstPublic("2021-01-11");
          dto.setLastUpdated("2021-01-11");

          return WebinXmlTransformation.createSampleTransformer()
              .transformForPresentation(dto, doc);
        });

    testPresentationTransformationForFile(
        "sample/sample02",
        xmlInputStream -> {
          SAMPLESETDocument doc = SAMPLESETDocument.Factory.parse(xmlInputStream);

          SampleTransformationDTO dto = new SampleTransformationDTO();
          dto.setSampleId("SRS308070");
          dto.setBioSampleId("SAMN00849598");
          dto.setSecondary(Collections.singleton("SRS308070"));
          dto.setCenterName("MBL");

          return WebinXmlTransformation.createSampleTransformer()
              .transformForPresentation(dto, doc);
        });

    testPresentationTransformationForFile(
        "sample/sample03",
        xmlInputStream -> {
          SAMPLESETDocument doc = SAMPLESETDocument.Factory.parse(xmlInputStream);

          SampleTransformationDTO dto = new SampleTransformationDTO();
          dto.setSampleId("ERS5524409");
          dto.setBioSampleId("SAMEA7777172");
          dto.setSubmissionId("ERA3206930");
          dto.setSubmissionAlias(
              "ena-SUBMISSION-Graz University of Technical-11-01-2021-14:11:54:887-1");
          dto.setCenterName("Graz University of Technical");

          return WebinXmlTransformation.createSampleTransformer()
              .transformForPresentation(dto, doc);
        });

    testPresentationTransformationForFile(
        "sample/sample_submitter_namespace_expansion",
        xmlInputStream -> {
          SAMPLESETDocument doc = SAMPLESETDocument.Factory.parse(xmlInputStream);

          SampleTransformationDTO dto = new SampleTransformationDTO();
          dto.setSampleId("ERS000004");
          dto.setBioSampleId("SAMEA749875");
          dto.setSubmissionId("ERA000011");
          dto.setSubmissionAlias("sgrp-sc-20080711-1");
          dto.setCenterName("Wellcome Sanger Institute");
          dto.setStatusId(4);
          dto.setFirstPublic("2010-02-26");
          dto.setLastUpdated("2018-03-09");

          return WebinXmlTransformation.createSampleTransformer()
              .transformForPresentation(dto, doc);
        });
  }

  @Test
  public void testExperimentXsltTransformation()
      throws IOException, SAXException, TransformerException {
    testForDir(
        "experiment/xslt",
        "EXPERIMENT",
        "EXPERIMENT_SET",
        WebinXmlTransformation.createExperimentTransformer());
  }

  @Test
  public void testExperimentPresentationTransformation() throws Exception {
    testPresentationTransformationForFile(
        "experiment/experiment",
        xmlInputStream -> {
          EXPERIMENTSETDocument doc = EXPERIMENTSETDocument.Factory.parse(xmlInputStream);

          ExperimentTransformationDTO dto = new ExperimentTransformationDTO();
          dto.setExperimentId("ERX4866416");
          dto.setSubmissionId("ERA3206930");
          dto.setInstrumentModel("Illumina MiSeq");
          dto.setCenterName("Graz University of Technical");
          dto.setSampleIds(Collections.singleton("ERS5524412"));
          dto.setSampleAccessions(Collections.singletonMap("ERS5524412", "SAMEA7777175"));
          dto.setStatusId(4);

          return WebinXmlTransformation.createExperimentTransformer()
              .transformForPresentation(dto, doc);
        });

    testPresentationTransformationForFile(
        "experiment/experiment_submitter_namespace_expansion",
        xmlInputStream -> {
          EXPERIMENTSETDocument doc = EXPERIMENTSETDocument.Factory.parse(xmlInputStream);

          ExperimentTransformationDTO dto = new ExperimentTransformationDTO();
          dto.setExperimentId("ERX002395");
          dto.setSubmissionId("ERA000196");
          dto.setInstrumentModel("Illumina Genome Analyzer II");
          dto.setCenterName("Wellcome Sanger Institute");
          dto.setSampleIds(Collections.singleton("ERS000119"));
          dto.setSampleAccessions(Collections.singletonMap("ERS000119", "SAMEA860413"));
          dto.setStatusId(4);

          return WebinXmlTransformation.createExperimentTransformer()
              .transformForPresentation(dto, doc);
        });
  }

  @Test
  public void testRunXsltTransformation() throws IOException, SAXException, TransformerException {
    testForDir("run/xslt", "RUN", "RUN_SET", WebinXmlTransformation.createRunTransformer());
  }

  @Test
  public void testRunPresentationTransformation() throws Exception {
    testPresentationTransformationForFile(
        "run/run",
        xmlInputStream -> {
          RUNSETDocument doc = RUNSETDocument.Factory.parse(xmlInputStream);

          RunTransformationDTO dto = new RunTransformationDTO();
          dto.setRunId("ERR5060344");
          dto.setStudyId("ERP121345");
          dto.setSampleIds(Collections.singleton("ERS5524409"));
          dto.setSubmissionId("ERA3206930");
          dto.setSubmissionAlias(
              "ena-SUBMISSION-Graz University of Technical-11-01-2021-14:11:54:887-1");
          dto.setExperimentTitle("Illumina MiSeq sequencing");
          dto.setCenterName("Graz University of Technical");
          dto.setStatusId(4);
          dto.setFirstPublic("2021-01-11");
          dto.setLastUpdated("2021-01-11");
          dto.setBaseCount(2334659l);
          dto.setSpotCount(5668l);

          return WebinXmlTransformation.createRunTransformer().transformForPresentation(dto, doc);
        });

    testPresentationTransformationForFile(
        "run/run_submitter_namespace_expansion",
        xmlInputStream -> {
          RUNSETDocument doc = RUNSETDocument.Factory.parse(xmlInputStream);

          RunTransformationDTO dto = new RunTransformationDTO();
          dto.setRunId("ERR4659819");
          dto.setStudyId("ERP121228");
          dto.setSampleIds(Collections.singleton("ERS5136096"));
          dto.setSubmissionId("ERA2939496");
          dto.setSubmissionAlias("ERP121228-sc-2020-10-06T16:41:38Z-2");
          dto.setExperimentTitle(
              "Illumina NovaSeq 6000 paired end sequencing; Illumina NovaSeq 6000 paired end sequencing; COG-UK/MILK-9E05B3/SANG:200930_A00948_0190_AHNFKGDRXX/1t241");
          dto.setCenterName("Wellcome Sanger Institute");
          dto.setStatusId(4);
          dto.setFirstPublic("2020-10-07");
          dto.setLastUpdated("2020-10-06");
          dto.setBaseCount(490101964l);
          dto.setSpotCount(2330393l);

          return WebinXmlTransformation.createRunTransformer().transformForPresentation(dto, doc);
        });
  }

  @Test
  public void testAnalsysisXsltTransformation()
      throws IOException, SAXException, TransformerException {
    testForDir(
        "analysis/xslt",
        "ANALYSIS",
        "ANALYSIS_SET",
        WebinXmlTransformation.createAnalysisTransformer());
  }

  @Test
  public void testAnalysisPresentationTransformation() throws Exception {
    testPresentationTransformationForFile(
        "analysis/analysis",
        xmlInputStream -> {
          ANALYSISSETDocument doc = ANALYSISSETDocument.Factory.parse(xmlInputStream);

          AnalysisTransformationDTO dto = new AnalysisTransformationDTO();
          dto.setAnalysisId("ERZ1701154");
          dto.setSubmissionId("ERA3271590");
          dto.setStudyId("ERP126153");
          dto.setCenterName("UNIVERSITY OF CALIFORNIA - DAVIS");
          dto.setStatusId(4);
          dto.setFirstPublic("2021-01-13");
          dto.setLastUpdated("2021-01-12");
          dto.setSampleAccessions(Collections.singletonMap("ERS3583620", "SAMEA104728908"));

          return WebinXmlTransformation.createAnalysisTransformer()
              .transformForPresentation(dto, doc);
        });

    testPresentationTransformationForFile(
        "analysis/analysis_submitter_namespace_expansion",
        xmlInputStream -> {
          ANALYSISSETDocument doc = ANALYSISSETDocument.Factory.parse(xmlInputStream);

          AnalysisTransformationDTO dto = new AnalysisTransformationDTO();
          dto.setAnalysisId("ERZ296129");
          dto.setSubmissionId("ERA610683");
          dto.setStudyId("ERP014691");
          dto.setCenterName("Wellcome Sanger Institute");
          dto.setStatusId(4);
          dto.setFirstPublic("2016-05-16");
          dto.setLastUpdated("2016-06-17");
          dto.setSampleAccessions(Collections.singletonMap("ERS1094210", "SAMEA3907076"));

          return WebinXmlTransformation.createAnalysisTransformer()
              .transformForPresentation(dto, doc);
        });
  }

  @Test
  public void testEGADacXsltTransformation()
      throws IOException, SAXException, TransformerException {
    testForDir("ega-dac/xslt", "DAC", "DAC_SET", WebinXmlTransformation.createEGADacTransformer());
  }

  @Test
  public void testEGAPolicyXsltTransformation()
      throws IOException, SAXException, TransformerException {
    testForDir(
        "ega-policy/xslt",
        "POLICY",
        "POLICY_SET",
        WebinXmlTransformation.createEGAPolicyTransformer());
  }

  @Test
  public void testEGADatasetXsltTransformation()
      throws IOException, SAXException, TransformerException {
    testForDir(
        "ega-dataset/xslt",
        "DATASET",
        "DATASETS",
        WebinXmlTransformation.createEGADatasetTransformer());
  }

  @Test
  public void testSubmissionXsltTransformation()
      throws IOException, SAXException, TransformerException {
    testForDir(
        "submission/xslt",
        "SUBMISSION",
        "SUBMISSION_SET",
        WebinXmlTransformation.createSubmissionTransformer());
  }

  @Test
  public void testSubmissionPresentationTransformation() throws Exception {
    testPresentationTransformationForFile(
        "submission/submission",
        xmlInputStream -> {
          SUBMISSIONSETDocument doc = SUBMISSIONSETDocument.Factory.parse(xmlInputStream);

          SubmissionTransformationDTO dto = new SubmissionTransformationDTO();
          dto.setSubmissionId("ERA3206932");
          dto.setSubmissionDate("11-jan-2021");
          dto.setSubmissionAlias("ena-SUBMISSION-SSI-11-01-2021-14:27:35:627-1");
          dto.setCenterName("Statens Serum Institut");

          return WebinXmlTransformation.createSubmissionTransformer()
              .transformForPresentation(dto, doc);
        });

    testPresentationTransformationForFile(
        "submission/submission_submitter_namespace_expansion",
        xmlInputStream -> {
          SUBMISSIONSETDocument doc = SUBMISSIONSETDocument.Factory.parse(xmlInputStream);

          SubmissionTransformationDTO dto = new SubmissionTransformationDTO();
          dto.setSubmissionId("ERA000024");
          dto.setSubmissionDate("12-aug-2008");
          dto.setSubmissionAlias("g1k-sc-20080812-2");
          dto.setCenterName("Wellcome Sanger Institute");

          return WebinXmlTransformation.createSubmissionTransformer()
              .transformForPresentation(dto, doc);
        });
  }

  private void testForDir(
      String dir, String rootElementName, String rootElementSetName, Transformer transformer)
      throws IOException, SAXException, TransformerException {
    File testDir = getResourceFile(dir);
    if (!testDir.isDirectory()) {
      throw new RuntimeException("Invalid resource directory: " + dir);
    }
    File[] files = testDir.listFiles();
    if (files == null) {
      throw new RuntimeException("No test files in directory: " + dir);
    }

    for (File file : files) {
      String name = file.getName();
      if (name.endsWith(INPUT_FILE_SUFFIX) && !name.endsWith(EXPECTED_FILE_SUFFIX)) {
        String filePrefix = name.replaceAll("\\.xml$", "");
        testForFile(
            dir + File.separator + filePrefix, rootElementName, rootElementSetName, transformer);
      } else if (!name.endsWith(EXPECTED_FILE_SUFFIX)) {
        throw new RuntimeException("Invalid test file name: " + name);
      }
    }
  }

  private void testForFile(
      String filePrefix, String rootElementName, String rootElementSetName, Transformer transformer)
      throws IOException, SAXException, TransformerException {
    String inputResourcePath = filePrefix + INPUT_FILE_SUFFIX;
    String expectedResourcePath = filePrefix + EXPECTED_FILE_SUFFIX;

    Document inputDocument = parseXmlFromInputStream(getResourceInputStream(inputResourcePath));
    addSetRootElementIfMissing(inputDocument, rootElementName, rootElementSetName);
    Document actualDocument = transformer.transform(inputDocument);

    String actual = writeXmlToString(actualDocument);
    String expected = getResourceContent(expectedResourcePath);

    testXmls(expected, actual, expectedResourcePath, inputResourcePath);
  }

  private void testPresentationTransformationForFile(
      String filePrefix, ApplyTransformation applyTransformation) throws Exception {

    String inputResourcePath = filePrefix + INPUT_FILE_SUFFIX;
    String expectedResourcePath = filePrefix + EXPECTED_FILE_SUFFIX;

    try (InputStream inputIs = getResourceInputStream(inputResourcePath)) {

      String actual = writeXmlToString(applyTransformation.apply(inputIs));
      String expected = getResourceContent(expectedResourcePath);

      testXmls(expected, actual, expectedResourcePath, inputResourcePath);
    }
  }

  private void testXmls(
      String expected, String actual, String expectedResourcePath, String actualResourcePath) {
    Diff diff =
        DiffBuilder.compare(Input.fromString(expected))
            .withTest(Input.fromString(actual))
            .ignoreComments()
            .ignoreWhitespace()
            .withDifferenceEvaluator(DifferenceEvaluators.Default)
            .build();

    if (diff.hasDifferences()) {
      System.out.println("First " + MAX_XML_DIFFERENCE_COUNT + " differences:");
      int differenceCount = 0;
      for (Difference difference : diff.getDifferences()) {
        if (++differenceCount > MAX_XML_DIFFERENCE_COUNT) {
          break;
        }
        System.out.println("    " + difference.toString());
      }

      // Normalise xmls and report comparison failure.
      throw new ComparisonFailure(
          String.format(
              "XMLs are different. InputFile : %s, ExpectedFile : %s",
              diff.getControlSource().toString(), diff.getTestSource().toString()),
          expected,
          actual);
    }

    if (expected.contains("<![CDATA[")) {
      Assert.assertTrue("XML is missing CDATA tag.", actual.contains("<![CDATA["));
    }
  }

  private static DocumentBuilder createDocumentBuilder() {
    try {
      return DocumentBuilderFactory.newInstance().newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

  private String getResourceContent(String resourcePath) throws IOException {
    return IOUtils.toString(getResourceInputStream(resourcePath), StandardCharsets.UTF_8.name());
  }

  private InputStream getResourceInputStream(String resourcePath) {
    return this.getClass().getResourceAsStream(BASE_RESOURCE_PATH + resourcePath);
  }

  private File getResourceFile(String resourcePath) {
    try {
      return new File(this.getClass().getResource(BASE_RESOURCE_PATH + resourcePath).toURI());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  private Document parseXmlFromString(String str) throws IOException, SAXException {
    return parseXmlFromInputStream(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
  }

  private Document parseXmlFromInputStream(InputStream is) throws IOException, SAXException {
    return DOCUMENT_BUILDER.parse(is);
  }

  private Document addSetRootElementIfMissing(
      Document doc, String rootElementName, String rootElementSetName) {
    // Add set root element if missing.
    Element root = doc.getDocumentElement();
    if (root.getTagName().equals(rootElementName)) {
      Element newRoot = doc.createElement(rootElementSetName);
      newRoot.appendChild(doc.getFirstChild());
      doc.appendChild(newRoot);
    }

    return doc;
  }

  private XmlOptions getParsingXmlOptions() {
    XmlOptions xmlOptions = new XmlOptions();
    xmlOptions.setLoadLineNumbers();
    xmlOptions.setCharacterEncoding("UTF-8");
    xmlOptions.setLoadStripComments();
    return xmlOptions;
  }

  public String writeXmlToString(Document document) throws TransformerException {
    // Create a TransformerFactory
    TransformerFactory transformerFactory = TransformerFactory.newInstance();

    // Create a Transformer
    javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();

    // Configure the transformer to output as XML
    transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Indent the output
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no"); // Include XML declaration

    // Create a DOMSource from the Document
    DOMSource source = new DOMSource(document);

    StringWriter stringWriter = new StringWriter();

    // Create a StreamResult to specify where to output the XML
    StreamResult result = new StreamResult(stringWriter);

    // Transform and print the XML
    transformer.transform(source, result);

    return stringWriter.toString();
  }

  public String writeXmlToString(XmlObject xmlObject) {
    XmlOptions options = new XmlOptions();
    options.setUseCDataBookmarks();
    options.setSavePrettyPrint();

    return xmlObject.xmlText(options);
  }

  private static interface ApplyTransformation {
    XmlObject apply(InputStream inputStream) throws Exception;
  }
}
