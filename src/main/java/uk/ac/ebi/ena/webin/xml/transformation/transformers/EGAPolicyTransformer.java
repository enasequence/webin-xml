package uk.ac.ebi.ena.webin.xml.transformation.transformers;

import org.w3c.dom.Document;
import uk.ac.ebi.ena.sra.xml.STUDYSETDocument;
import uk.ac.ebi.ena.webin.xml.transformation.WebinXmlTransformationException;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;

public class EGAPolicyTransformer extends AbstractTransformer {

    public EGAPolicyTransformer(Templates transformationTemplate) {
        super(transformationTemplate);
    }

    @Override
    public Document transform(Document document) throws WebinXmlTransformationException {
        try {
            document = applyTemplateTransformation(document);

            return document;
        } catch (TransformerException e) {
            throw new WebinXmlTransformationException("Error applying EGA policy transformation.", e);
        } catch (Exception e) {
            throw new WebinXmlTransformationException("Unexpected error while transforming EGA policy xml.", e);
        }
    }

    @Override
    public STUDYSETDocument transformForPresentation(Document document) throws WebinXmlTransformationException {
        return null;
    }
}