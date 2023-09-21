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

            convertPublicationLinksToProjectLinksRemovePublication(document);

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

    private void convertPublicationLinksToProjectLinksRemovePublication(Document doc) {
        List<Node> projects = getProjectNodes(doc);
        for (Node project : projects) {
            List<Node> xrefPublicationLinks = getXrefPublicationLinks(project);
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

    private List<Node> getProjectNodes(Document doc) {
        return getXmlNodes(doc, "/PROJECT_SET/PROJECT");
    }

    private List<Node> getXrefPublicationLinks(Node project) {
        return getXmlNodes(
            project, "./PUBLICATIONS/PUBLICATION/PUBLICATION_LINKS/PUBLICATION_LINK/XREF_LINK");
    }

    private Node getProjectLinksCreateIfNotExist(Document doc, Node project) {
        Node prj = getXmlNode(project, "./PROJECT_LINKS");
        Node projectLinks = null;
        Node projectAttributes = getXmlNode(project, "./PROJECT_ATTRIBUTES");
        if (prj == null) {
            projectLinks = doc.createElement("PROJECT_LINKS");
            if (projectAttributes != null) project.insertBefore(projectLinks, projectAttributes);
            else project.appendChild(projectLinks);
        }
        return prj != null ? prj : projectLinks;
    }
}
