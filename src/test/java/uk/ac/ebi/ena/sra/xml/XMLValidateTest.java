package uk.ac.ebi.ena.sra.xml;

import org.apache.logging.log4j.Logger;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.dsig.XMLObject;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by neilg on 18/05/15.
 */
public class XMLValidateTest {
    private XmlOptions xmlOptions;
    private ArrayList<XmlError> xmlErrors;
    private static final Logger logger = LogManager.getLogger(XMLValidateTest.class);

    public static final String SUBMISSION_FILE_RESOURCE_DIR = "/uk/ac/ebi/ena/sra/xml/submission";
    public static final String SAMPLE_FILE_RESOURCE_DIR = "/uk/ac/ebi/ena/sra/xml/sample";
    public static final String STUDY_FILE_RESOURCE_DIR = "/uk/ac/ebi/ena/sra/xml/study";
    public static final String PROJECT_FILE_RESOURCE_DIR = "/uk/ac/ebi/ena/sra/xml/project";
    public static final String EXPERIMENT_FILE_RESOURCE_DIR = "/uk/ac/ebi/ena/sra/xml/experiment";
    public static final String RUN_FILE_RESOURCE_DIR = "/uk/ac/ebi/ena/sra/xml/run";
    public static final String ANALYSIS_FILE_RESOURCE_DIR = "/uk/ac/ebi/ena/sra/xml/analysis";
    public static final String EGA_DATASET_FILE_RESOURCE_DIR = "/uk/ac/ebi/ena/sra/xml/ega_dataset";
    public static final String EGA_DAC_FILE_RESOURCE_DIR = "/uk/ac/ebi/ena/sra/xml/ega_dac";
    public static final String EGA_POLICY_FILE_RESOURCE_DIR = "/uk/ac/ebi/ena/sra/xml/ega_policy";

    @Before
    public void setUp() throws Exception {
        xmlErrors = new ArrayList<XmlError>();
        xmlOptions = new XmlOptions();
///        xmlOptions.setValidateOnSet();
        xmlOptions.setErrorListener(xmlErrors);
    }

    @Test
    public void testSubmissionXMLFiles () throws URISyntaxException, IOException, XmlException {
        testXMLFiles(SUBMISSION_FILE_RESOURCE_DIR);
    }

    @Test
    public void testStudyXMLFiles () throws URISyntaxException, IOException, XmlException {
        testXMLFiles(STUDY_FILE_RESOURCE_DIR);
    }

    @Test
    public void testProjectXMLFiles () throws URISyntaxException, IOException, XmlException {
        testXMLFiles(PROJECT_FILE_RESOURCE_DIR);
    }

    @Test
    public void testSampleXMLFiles () throws URISyntaxException, IOException, XmlException {
        testXMLFiles(SAMPLE_FILE_RESOURCE_DIR);
    }

    @Test
    public void testExperimentXMLFiles () throws URISyntaxException, IOException, XmlException {
        testXMLFiles(EXPERIMENT_FILE_RESOURCE_DIR);
    }

    @Test
    public void testRunXMLFiles () throws URISyntaxException, IOException, XmlException {
        testXMLFiles(RUN_FILE_RESOURCE_DIR);
    }

    @Test
    public void testAnalysisXMLFiles () throws URISyntaxException, IOException, XmlException {
        testXMLFiles(ANALYSIS_FILE_RESOURCE_DIR);
    }

    @Test
    public void testEGADACXMLFiles () throws URISyntaxException, IOException, XmlException {
        testXMLFiles(EGA_DAC_FILE_RESOURCE_DIR);
    }

    @Test
    public void testEGAPolicyXMLFiles () throws URISyntaxException, IOException, XmlException {
        testXMLFiles(EGA_POLICY_FILE_RESOURCE_DIR);
    }

    @Test
    public void testEGADatasetXMLFiles () throws URISyntaxException, IOException, XmlException {
        testXMLFiles(EGA_DATASET_FILE_RESOURCE_DIR);
    }

    public void testXMLFiles (String resourceDir) throws URISyntaxException, IOException, XmlException {
        final List<File> fileListInResourceDir = getFileListInResourceDir(resourceDir);
        for (File file : fileListInResourceDir) {
            XmlObject xmlObject = XmlObject.Factory.parse(file);
            logger.info("Testing file " + file.getName() + " schema type " + xmlObject.schemaType().toString());
            validateDocument(xmlObject);
        }
    }

    public boolean validateDocument (XmlObject xmlObject) {
        boolean valid = xmlObject.validate(xmlOptions);
        if (!valid) {
            logger.error(xmlObject.schemaType() + " failed to validate ");
            for (XmlError xmlError : xmlErrors) {
                logger.error(xmlError);
            }
        }
        assertTrue(valid);
        return valid;
    }


    public static List<File> getFileListInResourceDir (String resourceDir) throws URISyntaxException {
        final URL resource = XMLValidateTest.class.getResource(resourceDir);
        List<File> fileList = new ArrayList<File>();
        if (resource != null) {
            File file = new File(resource.toURI());
            if (file.canRead() && file.isDirectory()) {
                for (String fileName : file.list()) {
                    File subFile = new File(file,fileName);
                    if (subFile.canRead() && fileName.endsWith(".xml")) {
                        logger.info("Testing file " + fileName + " in package dir " + resourceDir);
                        fileList.add(subFile);
                    }
                }
            }
        }
        return fileList;
    }

}
