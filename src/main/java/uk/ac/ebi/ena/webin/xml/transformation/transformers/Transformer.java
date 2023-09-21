package uk.ac.ebi.ena.webin.xml.transformation.transformers;

import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Document;
import uk.ac.ebi.ena.webin.xml.transformation.WebinXmlTransformationException;

public interface Transformer<T extends XmlObject> {

    Document transform(Document document) throws WebinXmlTransformationException;

    T transformForPresentation(Document document) throws WebinXmlTransformationException;
}
