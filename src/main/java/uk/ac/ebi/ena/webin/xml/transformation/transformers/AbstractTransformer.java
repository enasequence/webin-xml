/*
 * Copyright 2023 EMBL - European Bioinformatics Institute
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package uk.ac.ebi.ena.webin.xml.transformation.transformers;

import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.xmlbeans.CDataBookmark;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.ac.ebi.ena.sra.xml.AttributeType;
import uk.ac.ebi.ena.sra.xml.IdentifierType;
import uk.ac.ebi.ena.sra.xml.LinkType;
import uk.ac.ebi.ena.sra.xml.ObjectType;
import uk.ac.ebi.ena.sra.xml.QualifiedNameType;
import uk.ac.ebi.ena.sra.xml.XRefType;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.dtos.PresentationTransformationDTO;

public abstract class AbstractTransformer {

  public static final String ACCESSION_MASK = "#";

  public static final String ENA_FASTQ_FILES_TAG = "ENA-FASTQ-FILES";

  public static final String ENA_SUBMITTED_FILES_TAG = "ENA-SUBMITTED-FILES";

  public static final String ENA_FASTQ_FILES_URL_PREFORMAT =
      "https://www.ebi.ac.uk/ena/portal/api/filereport?accession=%1$s&result=read_run&fields=run_accession,fastq_ftp,fastq_md5,fastq_bytes";

  public static final String ENA_SUBMITTED_FILES_URL_PREFORMAT =
      "https://www.ebi.ac.uk/ena/portal/api/filereport?accession=%1$s&result=read_run&fields=run_accession,submitted_ftp,submitted_md5,submitted_bytes,submitted_format";

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

  // Following is related to presentation

  /**
   * - Set/override center and broker names in XML document if they are available in the dto.<br>
   * - Depending on given SRA object ID, sets either NCBI or DDBJ as broker names.<br>
   * - Remove broker name field if it is blank.
   */
  protected <T extends PresentationTransformationDTO, U extends ObjectType> void transformCommon(
      T dto, String sraObjId, U objectType) {

    if (dto.getCenterName() != null) {
      objectType.setCenterName(dto.getCenterName());
    }
    if (dto.getBrokerName() != null) {
      objectType.setBrokerName(dto.getBrokerName());
    }

    // EMD-6683
    setNcbiDdbjBrokerNamesIfApplicable(objectType, sraObjId);

    unsetBrokerNameIfBlank(objectType);
  }

  private <T extends ObjectType> void setNcbiDdbjBrokerNamesIfApplicable(
      T enaObject, String objectId) {
    if (enaObject.getBrokerName() == null || enaObject.getBrokerName().isEmpty()) {
      if (objectId.startsWith("SRP") || objectId.startsWith("SRS")) {
        enaObject.setBrokerName("NCBI");
      } else if (objectId.startsWith("DRP") || objectId.startsWith("DRS")) {
        enaObject.setBrokerName("DDBJ");
      }
    }
  }

  private <T extends ObjectType> void unsetBrokerNameIfBlank(T enaObject) {
    if (enaObject.isSetBrokerName()
        && (null == enaObject.getBrokerName() || enaObject.getBrokerName().isEmpty()))
      enaObject.unsetBrokerName();
  }

  protected <T extends ObjectType> void unsetIdentifiersSubmitterIdIfBlankAlias(T enaObject) {
    if (null == enaObject.getAlias() || enaObject.getAlias().isEmpty()) {
      if (enaObject.getIDENTIFIERS().isSetSUBMITTERID()) {
        enaObject.getIDENTIFIERS().unsetSUBMITTERID();
      }
    }
  }

  protected void injectSecondaries(IdentifierType identifiers, Set<String> secondary_set) {
    if (null != identifiers) {
      for (String secondaryId : secondary_set)
        identifiers.addNewSECONDARYID().setStringValue(secondaryId);
    }
  }

  /**
   * Sets center name as namespace attribute of submitter ID if center name and submitter ID
   * information is present.
   */
  protected void fixIdentifiers(ObjectType objectType) {
    IdentifierType identifierType = objectType.getIDENTIFIERS();
    if (identifierType != null) {
      QualifiedNameType submitterIdNameType = identifierType.getSUBMITTERID();
      if (submitterIdNameType != null && objectType.isSetCenterName()) {
        submitterIdNameType.setNamespace(objectType.getCenterName());
      }
    }
  }

  /** EMD-1967: Removing LINKS(except XREF pubmed) */
  protected <T extends XmlObject> void retainOnlyPubmedAndURLLinks(T linkObj, LinkType[] links) {
    if (links != null && links.length > 0) {
      for (LinkType link : links) {
        Node nodeLINK = link.getDomNode();
        if (link.isSetURLLINK()) {
          // commenting EMD-4854
          // link.unsetURLLINK();
          // linkObj.getDomNode().removeChild(nodeLINK);
        } else if (link.isSetXREFLINK()) {
          if (link.getXREFLINK() != null
              && link.getXREFLINK().getDB() != null
              && link.getXREFLINK().getDB().toLowerCase().matches("pubmed")) {
            continue;
          } else {
            link.unsetXREFLINK();
            linkObj.getDomNode().removeChild(nodeLINK);
          }
        }
      }
    }
  }

  /** EMD-1967: Removing LINKS(except XREF pubmed) */
  protected <T extends XmlObject> void retainOnlyPubmedLinks(T linkObj, LinkType[] links) {
    if (links != null && links.length > 0) {
      for (LinkType link : links) {
        Node nodeLINK = link.getDomNode();
        if (link.isSetURLLINK()) {
          link.unsetURLLINK();
          linkObj.getDomNode().removeChild(nodeLINK);

        } else if (link.isSetXREFLINK()) {
          if (link.getXREFLINK() != null
              && link.getXREFLINK().getDB() != null
              && link.getXREFLINK().getDB().toLowerCase().matches("pubmed")) {
            continue;
          } else {
            link.unsetXREFLINK();
            linkObj.getDomNode().removeChild(nodeLINK);
          }
        }
      }
    }
  }

  protected void appendArrayExpressLink(String alias, Supplier<XRefType> xreflinkSupplier) {
    String aeAlias = extractAE(alias);
    if (alias != null && !alias.equals(aeAlias)) {
      appendLink(xreflinkSupplier.get(), "ARRAYEXPRESS", aeAlias, false);
    }
  }

  protected void appendLink(XRefType xreflink, String db, String id, boolean cdata) {
    xreflink.setDB(db);
    xreflink.setID(id);

    if (!cdata) {
      return;
    }

    // add CDATA tag
    XmlCursor cursor = xreflink.xgetID().newCursor();
    cursor.toFirstContentToken();
    cursor.setBookmark(CDataBookmark.CDATA_BOOKMARK);
    cursor.dispose();
  }

  protected <T extends PresentationTransformationDTO> void addFirstPublicLastUpdateAttributes(
      T inpObj, Supplier<AttributeType> attributeTypeSupplier) {
    if (inpObj.getFirstPublic() != null)
      appendAttribute(attributeTypeSupplier, "ENA-FIRST-PUBLIC", inpObj.getFirstPublic());

    if (inpObj.getLastUpdated() != null)
      appendAttribute(attributeTypeSupplier, "ENA-LAST-UPDATE", inpObj.getLastUpdated());
  }

  protected <T extends PresentationTransformationDTO> void addEnaStatusIdAttribute(
      T inpObj, Supplier<AttributeType> attributeTypeSupplier) {
    if (inpObj.getStatusId() != null) {
      appendAttribute(attributeTypeSupplier, "ENA-STATUS-ID", inpObj.getStatusId().toString());
    }
  }

  protected void appendAttribute(
      Supplier<AttributeType> attributeTypeSupplier, String tag, String value) {
    if (tag == null || tag.replaceAll(" ", "").isEmpty()) {
      return;
    }

    AttributeType attributeType = attributeTypeSupplier.get();
    attributeType.setTAG(tag);
    attributeType.setVALUE(value);
  }

  protected List<String> getRangeList(Collection<String> accession_list) {
    return getRanges(accession_list).entrySet().stream()
        .sorted((e1, e2) -> e1.getKey().toString().compareTo(e2.getKey().toString()))
        .map(Map.Entry::getValue)
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  private String extractAE(String alias) {
    if (null != alias) {
      Matcher m =
          Pattern.compile("GEO\\s*(Series\\s*accession)?\\s*:\\s*(GSE\\d+).*").matcher(alias);
      if (m.find()) {
        return m.group(2).replace("GSE", "E-GEOD-");
      }
    }

    return alias;
  }

  private Map<Map.Entry<String, String>, List<String>> getRanges(
      Collection<String> accession_list) {
    return accession_list.stream()
        .collect(
            Collectors.groupingBy(
                e ->
                    new AbstractMap.SimpleEntry<>(
                        matchSplit(e)[0],
                        Stream.generate(() -> ACCESSION_MASK)
                            .limit(e.length())
                            .collect(Collectors.joining())),
                Collectors.mapping(e -> matchSplit(e)[1], Collectors.toList())))
        .entrySet()
        .stream()
        .map(
            (e) ->
                new AbstractMap.SimpleEntry<Map.Entry<String, String>, List<String>>(
                    e.getKey(),
                    e.getValue().stream()
                        .sorted(
                            (o1, o2) ->
                                Integer.compare(Integer.parseInt(o1, 10), Integer.parseInt(o2, 10)))
                        .map(i -> Arrays.asList(new AbstractMap.SimpleEntry<>(i, i)))
                        .reduce(new ArrayList<>(), AbstractTransformer::entryReduce)
                        .stream()
                        .map(
                            i ->
                                i.getKey() == i.getValue()
                                    ? e.getKey().getKey() + i.getKey()
                                    : e.getKey().getKey()
                                        + i.getKey()
                                        + '-'
                                        + e.getKey().getKey()
                                        + i.getValue())
                        .collect(Collectors.toList())))
        .collect(
            Collectors.toMap(
                AbstractMap.SimpleEntry::getKey,
                AbstractMap.SimpleEntry::getValue,
                (v1, v2) -> {
                  v1.addAll(v2);
                  return v1.stream().sorted().collect(Collectors.toList());
                }));
  }

  private String[] matchSplit(String s) {
    Matcher m = null;
    if ((m = Pattern.compile("(\\D+)(\\d+)").matcher(s)).find() && 2 == m.groupCount())
      return new String[] {m.group(1), m.group(2)};

    throw new RuntimeException("Unable to split accession: " + s);
  }

  private static List<AbstractMap.SimpleEntry<String, String>> entryReduce(
      List<AbstractMap.SimpleEntry<String, String>> a,
      List<AbstractMap.SimpleEntry<String, String>> e) {
    if (!a.isEmpty()
        && 1
            >= -Integer.parseInt(a.get(a.size() - 1).getValue(), 10)
                + Integer.parseInt(e.get(0).getValue(), 10))
      a.get(a.size() - 1).setValue(e.get(0).getValue());
    else a.addAll(e);
    return a;
  }
}
