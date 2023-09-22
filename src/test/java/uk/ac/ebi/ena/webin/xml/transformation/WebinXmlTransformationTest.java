package uk.ac.ebi.ena.webin.xml.transformation;

import difflib.DiffRow;
import difflib.DiffRowGenerator;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;
import org.xmlunit.diff.DifferenceEvaluators;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.Transformer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.Arrays;

public class WebinXmlTransformationTest {

    private static final String BASE_RESOURCE_PATH = "/uk/ac/ebi/ena/webin/xml/transformation/";
    private static final String INPUT_FILE_SUFFIX = ".xml";
    private static final String EXPECTED_FILE_SUFFIX = "-expected.xml";
    private static final DocumentBuilder DOCUMENT_BUILDER = createDocumentBuilder();
    private static final int MAX_XML_DIFFERENCE_COUNT = 5;

    @Test
    public void testStudyTransformation() throws IOException, SAXException, TransformerException {
        testForDir("study", "STUDY", "STUDY_SET", WebinXmlTransformation.createStudyTransformer());
    }

    @Test
    public void testProjectTransformation() throws IOException, SAXException, TransformerException {
        testForDir("project", "PROJECT", "PROJECT_SET", WebinXmlTransformation.createProjectTransformer());
    }

    @Test
    public void testSampleTransformation() throws IOException, SAXException, TransformerException {
        testForDir("sample", "SAMPLE", "SAMPLE_SET", WebinXmlTransformation.createSampleTransformer());
    }

    @Test
    public void testExperimentTransformation() throws IOException, SAXException, TransformerException {
        testForDir("experiment", "EXPERIMENT", "EXPERIMENT_SET", WebinXmlTransformation.createExperimentTransformer());
    }

    @Test
    public void testRunTransformation() throws IOException, SAXException, TransformerException {
        testForDir("run", "RUN", "RUN_SET", WebinXmlTransformation.createRunTransformer());
    }

    @Test
    public void testAnalysisTransformation() throws IOException, SAXException, TransformerException {
        testForDir("analysis", "ANALYSIS", "ANALYSIS_SET", WebinXmlTransformation.createAnalysisTransformer());
    }

    @Test
    public void testEGADacTransformation() throws IOException, SAXException, TransformerException {
        testForDir("ega-dac", "DAC", "DAC_SET", WebinXmlTransformation.createEGADacTransformer());
    }

    @Test
    public void testEGAPolicyTransformation() throws IOException, SAXException, TransformerException {
        testForDir("ega-policy", "POLICY", "POLICY_SET", WebinXmlTransformation.createEGAPolicyTransformer());
    }

    @Test
    public void testEGADatasetTransformation() throws IOException, SAXException, TransformerException {
        testForDir("ega-dataset", "DATASET", "DATASETS", WebinXmlTransformation.createEGADatasetTransformer());
    }

    @Test
    public void testSubmissionTransformation() throws IOException, SAXException, TransformerException {
        testForDir("submission", "SUBMISSION", "SUBMISSION_SET", WebinXmlTransformation.createSubmissionTransformer());
    }

    private void testForDir(String dir, String rootElementName, String rootElementSetName, Transformer transformer) throws IOException, SAXException, TransformerException {
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
            if (name.endsWith(INPUT_FILE_SUFFIX) &&
                    !name.endsWith(EXPECTED_FILE_SUFFIX)) {
                String filePrefix = name.replaceAll("\\.xml$", "");
                testForFile(dir + File.separator + filePrefix, rootElementName, rootElementSetName, transformer);
            } else if (!name.endsWith(EXPECTED_FILE_SUFFIX)) {
                throw new RuntimeException("Invalid test file name: " + name);
            }
        }
    }

    private void testForFile(String filePrefix, String rootElementName, String rootElementSetName, Transformer transformer) throws IOException, SAXException, TransformerException {
        String inputFile = filePrefix + INPUT_FILE_SUFFIX;
        String expectedFile = filePrefix + EXPECTED_FILE_SUFFIX;

        System.out.println("Testing input file: " + inputFile);

        Document document = parseXmlFromInputStream(getResourceInputStream(inputFile));
        addSetRootElementIfMissing(document, rootElementName, rootElementSetName);
        document = transformer.transform(document);

        String actual = toString(document);
        String expected = getResourceContent(expectedFile);

        Diff diff = DiffBuilder.compare(Input.fromString(expected))
                .withTest(Input.fromString(actual))
                .ignoreComments()
                .ignoreWhitespace()
             .withDifferenceEvaluator(DifferenceEvaluators.Default)
                .build();

        int differenceCount = 0;
        for (Difference difference :  diff.getDifferences()) {
            if (++differenceCount > MAX_XML_DIFFERENCE_COUNT) {
                break;
            }
            System.out.println("Difference: " + difference.toString());
        }

        if (diff.hasDifferences()) {
            DiffRowGenerator generator =
                    (new DiffRowGenerator.Builder())
                            .ignoreWhiteSpaces(true)
                            .ignoreBlankLines(true)
                            .showInlineDiffs(true)
                            .columnWidth(Integer.MAX_VALUE)
                            .build();

            for (DiffRow diffRow : generator.generateDiffRows(
                    Arrays.asList(expected.split("\n")),
                    Arrays.asList(actual.split("\n")))) {
                if (diffRow.getTag() != DiffRow.Tag.EQUAL) {
                    System.out.println("Difference: " + diffRow.getTag());
                    System.out.println("Expected: '" + StringEscapeUtils.unescapeHtml4(diffRow.getOldLine()) + "'");
                    System.out.println("Actual: '" + StringEscapeUtils.unescapeHtml4(diffRow.getNewLine()) + "'");
                }
            }
        }

        Assert.assertFalse(diff.hasDifferences());
    }

    private static DocumentBuilder createDocumentBuilder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private String getResourceContent(String resourcePath) throws IOException {
        return IOUtils.toString(getResourceInputStream(resourcePath));
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

    private Document parseXmlFromInputStream(InputStream is) throws IOException, SAXException {
        return DOCUMENT_BUILDER.parse(is);
    }

    private Document addSetRootElementIfMissing(Document doc, String rootElementName, String rootElementSetName) {
        // Add set root element if missing.
        Element root = doc.getDocumentElement();
        if (root.getTagName().equals(rootElementName)) {
            Element newRoot = doc.createElement(rootElementSetName);
            newRoot.appendChild(doc.getFirstChild());
            doc.appendChild(newRoot);
        }

        return doc;
    }

    public static String toString(Document document) throws TransformerException {
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
}