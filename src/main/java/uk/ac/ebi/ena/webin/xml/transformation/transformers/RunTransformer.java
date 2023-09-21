package uk.ac.ebi.ena.webin.xml.transformation.transformers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import uk.ac.ebi.ena.sra.xml.STUDYSETDocument;
import uk.ac.ebi.ena.webin.xml.transformation.WebinXmlTransformationException;
import uk.ac.ebi.ena.webin.xml.transformation.fixers.InstrumentModelEnumFixer;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import java.util.List;

public class RunTransformer extends AbstractTransformer {

    private final InstrumentModelEnumFixer instrumentModelEnumFixer = new InstrumentModelEnumFixer();

    public RunTransformer(Templates transformationTemplate) {
        super(transformationTemplate);
    }

    @Override
    public Document transform(Document document) throws WebinXmlTransformationException {
        try {
            document = applyTemplateTransformation(document);

            // Add FILE/@checksum_method ('MD5') if missing.
            for (Node file :
                getXmlNodes(document, "/RUN_SET/RUN/DATA_BLOCK/FILES/FILE[not(@checksum_method)]")) {
                ((Element) file).setAttribute("checksum_method", "MD5");
            }
            // Add FILE/@checksum if missing.
            for (Node file : getXmlNodes(document, "/RUN_SET/RUN/DATA_BLOCK/FILES/FILE[not(@checksum)]")) {
                ((Element) file).setAttribute("checksum", "");
            }

            // Merge FILEs from all DATA_BLOCKs together.
            for (Node run : getXmlNodes(document, "/RUN_SET/RUN")) {
                List<Node> dataBlocks = getXmlNodes(run, "DATA_BLOCK");
                if (dataBlocks.size() > 1) {
                    Node firstDataBlock = dataBlocks.get(0);
                    Node firstFiles = getXmlNode(firstDataBlock, "FILES");
                    if (firstFiles != null) {
                        for (int i = 1; i < dataBlocks.size(); ++i) {
                            Node otherDataBlock = dataBlocks.get(i);
                            for (Node file : getXmlNodes(otherDataBlock, "FILES/FILE")) {
                                firstFiles.appendChild(file);
                            }
                            run.removeChild(otherDataBlock);
                        }
                    }
                }
            }

            // Change file types.
            for (Node filetype :
                getXmlNodes(
                    document,
                    "/RUN_SET/RUN/DATA_BLOCK/FILES/FILE[@filetype='Illumina_native_fastq']/@filetype")) {
                filetype.setNodeValue("fastq");
            }

            fixInstrumentModel(document, "/RUN_SET/RUN/PLATFORM");

            return document;
        } catch (TransformerException e) {
            throw new WebinXmlTransformationException("Error applying run transformation.", e);
        } catch (Exception e) {
            throw new WebinXmlTransformationException("Unexpected error while transforming run xml.", e);
        }
    }

    @Override
    public STUDYSETDocument transformForPresentation(Document document) throws WebinXmlTransformationException {
        return null;
    }

    private void fixInstrumentModel(Node doc, String platformPath) {
        for (Node instrumentModel : getXmlNodes(doc, platformPath + "/*/INSTRUMENT_MODEL")) {
            instrumentModelEnumFixer.fixNodeValue(instrumentModel);
        }
    }
}
