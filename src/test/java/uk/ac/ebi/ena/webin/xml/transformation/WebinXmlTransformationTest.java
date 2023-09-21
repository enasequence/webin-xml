package uk.ac.ebi.ena.webin.xml.transformation;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.Transformer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class WebinXmlTransformationTest {

    private static final String BASE_RESOURCE_PATH = "/uk/ac/ebi/ena/webin/xml/transformation/";

    private static final DocumentBuilder DOCUMENT_BUILDER = createDocumentBuilder();

    @Test
    public void testStudyTransformation() throws IOException, SAXException, TransformerException {
        testFor("study", "STUDY", "STUDY_SET", WebinXmlTransformation.createStudyTransformer());
    }

    @Test
    public void testProjectTransformation() throws IOException, SAXException, TransformerException {
        testFor("project", "PROJECT", "PROJECT_SET", WebinXmlTransformation.createProjectTransformer());
    }

    @Test
    public void testSampleTransformation() throws IOException, SAXException, TransformerException {
        testFor("sample", "SAMPLE", "SAMPLE_SET", WebinXmlTransformation.createSampleTransformer());
    }

    @Test
    public void testExperimentTransformation() throws IOException, SAXException, TransformerException {
        testFor("experiment", "EXPERIMENT", "EXPERIMENT_SET", WebinXmlTransformation.createExperimentTransformer());
    }

    @Test
    public void testRunTransformation() throws IOException, SAXException, TransformerException {
        testFor("run", "RUN", "RUN_SET", WebinXmlTransformation.createRunTransformer());
    }

    @Test
    public void testAnalysisTransformation() throws IOException, SAXException, TransformerException {
        testFor("analysis", "ANALYSIS", "ANALYSIS_SET", WebinXmlTransformation.createAnalysisTransformer());
    }

    @Test
    public void testEGADacTransformation() throws IOException, SAXException, TransformerException {
        testFor("ega_dac", "DAC", "DAC_SET", WebinXmlTransformation.createEGADacTransformer());
    }

    @Test
    public void testEGAPolicyTransformation() throws IOException, SAXException, TransformerException {
        testFor("ega_policy", "POLICY", "POLICY_SET", WebinXmlTransformation.createEGAPolicyTransformer());
    }

    @Test
    public void testEGADatasetTransformation() throws IOException, SAXException, TransformerException {
        testFor("ega_dataset", "DATASET", "DATASETS", WebinXmlTransformation.createEGADatasetTransformer());
    }

    @Test
    public void testSubmissionTransformation() throws IOException, SAXException, TransformerException {
        testFor("submission", "SUBMISSION", "SUBMISSION_SET", WebinXmlTransformation.createSubmissionTransformer());
    }

    private void testFor(String filePrefix, String rootElementName, String rootElementSetName, Transformer transformer) throws IOException, SAXException, TransformerException {
        String inputFile = filePrefix + ".xml";
        String expectedFile = filePrefix + "-expected.xml";

        Document document = parseXmlFromInputStream(getResourceInputStream(inputFile));

        addSetRootElementIfMissing(document, rootElementName, rootElementSetName);

        document = transformer.transform(document);

        String actual = toString(document);

        String expected = getResourceContent(expectedFile);

        Assert.assertEquals(expected, actual);
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
        return WebinXmlTransformationTest.class.getResourceAsStream(BASE_RESOURCE_PATH + resourcePath);
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
