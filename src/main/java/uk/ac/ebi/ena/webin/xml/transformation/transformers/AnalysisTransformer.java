package uk.ac.ebi.ena.webin.xml.transformation.transformers;

import org.w3c.dom.Document;
import uk.ac.ebi.ena.sra.xml.STUDYSETDocument;
import uk.ac.ebi.ena.webin.xml.transformation.WebinXmlTransformationException;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;

public class AnalysisTransformer extends AbstractTransformer {

    public AnalysisTransformer(Templates transformationTemplate) {
        super(transformationTemplate);
    }

    @Override
    public Document transform(Document document) throws WebinXmlTransformationException {
        try {
            document = applyTemplateTransformation(document);

            return document;
        } catch (TransformerException e) {
            throw new WebinXmlTransformationException("Error applying analysis transformation.", e);
        } catch (Exception e) {
            throw new WebinXmlTransformationException("Unexpected error while transforming analysis xml.", e);
        }
    }

    @Override
    public STUDYSETDocument transformForPresentation(Document document) throws WebinXmlTransformationException {
        // SRAAnalysisParser.modifySubmittedXml() should go here as these modifications are performed on the
        // parsed XmlObject and not the Document.

        return null;
    }
}
