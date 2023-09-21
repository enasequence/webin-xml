package uk.ac.ebi.ena.webin.xml.transformation.transformers;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import uk.ac.ebi.ena.sra.xml.STUDYSETDocument;
import uk.ac.ebi.ena.webin.xml.transformation.WebinXmlTransformationException;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import java.util.List;

public class StudyTransformer extends AbstractTransformer {

    public StudyTransformer(Templates transformationTemplate) {
        super(transformationTemplate);
    }

    @Override
    public Document transform(Document document) throws WebinXmlTransformationException {
        try {
            document = applyTemplateTransformation(document);

            List<Node> nodeList = getXmlNodes(
                document, "/STUDY_SET/STUDY/DESCRIPTOR/STUDY_TYPE[@existing_study_type='Transcriptome Sequencing']/@existing_study_type");

            for (Node existingStudyType : nodeList) {
                existingStudyType.setNodeValue("Transcriptome Analysis");
            }

            return document;
        } catch (TransformerException e) {
            throw new WebinXmlTransformationException("Error applying study transformation.", e);
        } catch (Exception e) {
            throw new WebinXmlTransformationException("Unexpected error while transforming study xml.", e);
        }
    }

    @Override
    public STUDYSETDocument transformForPresentation(Document document) throws WebinXmlTransformationException {
        return null;
    }
}
