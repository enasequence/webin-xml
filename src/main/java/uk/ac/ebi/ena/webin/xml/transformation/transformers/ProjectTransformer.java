package uk.ac.ebi.ena.webin.xml.transformation.transformers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import uk.ac.ebi.ena.sra.xml.STUDYSETDocument;
import uk.ac.ebi.ena.webin.xml.transformation.WebinXmlTransformationException;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import java.util.List;

public class ProjectTransformer extends AbstractTransformer {

    public ProjectTransformer(Templates transformationTemplate) {
        super(transformationTemplate);
    }

    @Override
    public Document transform(Document document) throws WebinXmlTransformationException {
        try {
            document = applyTemplateTransformation(document);

            convertPublicationLinksToProjectLinksAndRemovePublication(document);

            return document;
        } catch (TransformerException e) {
            throw new WebinXmlTransformationException("Error applying project transformation.", e);
        } catch (Exception e) {
            throw new WebinXmlTransformationException("Unexpected error while transforming project xml.", e);
        }
    }

    @Override
    public STUDYSETDocument transformForPresentation(Document document) throws WebinXmlTransformationException {
        return null;
    }

    private void convertPublicationLinksToProjectLinksAndRemovePublication(Document doc) {
        List<Node> projects =  getXmlNodes(doc, "/PROJECT_SET/PROJECT");
        for (Node project : projects) {
            List<Node> xrefPublicationLinks = getXmlNodes(project,
                    "./PUBLICATIONS/PUBLICATION/PUBLICATION_LINKS/PUBLICATION_LINK/XREF_LINK");
            if (!xrefPublicationLinks.isEmpty()) {
                Node projectLinks = getProjectLinksCreateIfNotExist(doc, project);
                for (Node xrefPublicationLink : xrefPublicationLinks) {
                    Element element = doc.createElement("PROJECT_LINK");
                    element.appendChild(xrefPublicationLink);
                    projectLinks.appendChild(element);
                }
            }
            Node publications = getXmlNode(project, "./PUBLICATIONS");
            if (publications != null) project.removeChild(publications);
        }
    }

    private Node getProjectLinksCreateIfNotExist(Document doc, Node project) {
        Node projectLinks = getXmlNode(project, "./PROJECT_LINKS");
        if (projectLinks == null) {
            projectLinks = doc.createElement("PROJECT_LINKS");
            Node projectAttributes = getXmlNode(project, "./PROJECT_ATTRIBUTES");
            if (projectAttributes != null) {
                project.insertBefore(projectLinks, projectAttributes);
            }
            else {
                project.appendChild(projectLinks);
            }
        }
        return projectLinks;
    }
}
