package uk.ac.ebi.ena.webin.xml.transformation.transformers;

import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.AbstractList;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

public abstract class AbstractTransformer implements Transformer {

    protected static final XmlOptions XML_OPTIONS = createXmlOptions();

    protected final Templates transformationTemplate;

    protected AbstractTransformer(Templates transformationTemplate) {
        this.transformationTemplate = transformationTemplate;
    }

    /**
     * @param document
     * @return Returns a new object.
     * @throws TransformerException
     */
    protected Document applyTemplateTransformation(Document document) throws TransformerException {
        DOMSource source = new DOMSource(document);

        DOMResult result = new DOMResult();

        javax.xml.transform.Transformer transformer = transformationTemplate.newTransformer();
        transformer.transform(source, result);

        return (Document) result.getNode();
    }

    protected Node getXmlNode(Node doc, String path) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xPath.compile(path);
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            return (nodes.getLength() > 0) ? nodes.item(0) : null;
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    protected List<Node> getXmlNodes(Node doc, String path) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xPath.compile(path);
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            return createNodeListWrapper((NodeList) result);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Node> createNodeListWrapper(NodeList n) {
        return n.getLength() == 0 ? Collections.emptyList() : new NodeListWrapper(n);
    }

    private static XmlOptions createXmlOptions() {
        XmlOptions xmlOptions = new XmlOptions();
        xmlOptions.setLoadLineNumbers(XmlOptions.LOAD_LINE_NUMBERS);
        xmlOptions.setCharacterEncoding("UTF-8");
        xmlOptions.setLoadStripComments();
        return xmlOptions;
    }

    protected static final class NodeListWrapper extends AbstractList<Node> implements RandomAccess {
        private final NodeList n;

        NodeListWrapper(NodeList n) {
            this.n = n;
        }

        public Node get(int index) {
            return n.item(index);
        }

        public int size() {
            return n.getLength();
        }
    }
}
