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

import java.util.List;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import uk.ac.ebi.ena.sra.xml.AttributeType;
import uk.ac.ebi.ena.sra.xml.PROJECTSETDocument;
import uk.ac.ebi.ena.sra.xml.ProjectType;
import uk.ac.ebi.ena.sra.xml.XRefType;
import uk.ac.ebi.ena.webin.xml.transformation.WebinXmlTransformationException;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.dtos.ProjectTransformationDTO;

public class ProjectTransformer extends AbstractTransformer
    implements Transformer<ProjectTransformationDTO, PROJECTSETDocument> {

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
      throw new WebinXmlTransformationException(
          "Unexpected error while transforming project xml.", e);
    }
  }

  @Override
  public PROJECTSETDocument transformForPresentation(
      ProjectTransformationDTO projectDto, PROJECTSETDocument document)
      throws WebinXmlTransformationException {

    ProjectType projectType = document.getPROJECTSET().getPROJECTArray()[0];

    transformCommon(projectDto, projectDto.getProjectId(), projectType);

    transformIdentifiers(projectDto, projectType);

    if (projectType.isSetFirstPublic()) {
      projectType.unsetFirstPublic();
    }

    transformLinks(projectDto, projectType);

    if (!projectDto.getChildren().isEmpty()) {
      if (null == projectType.getRELATEDPROJECTS()) projectType.addNewRELATEDPROJECTS();

      projectDto
          .getChildren()
          .forEach(
              e ->
                  projectType
                      .getRELATEDPROJECTS()
                      .addNewRELATEDPROJECT()
                      .addNewCHILDPROJECT()
                      .setAccession(e));
    }

    if (!projectDto.getChildren().isEmpty()) {
      if (null == projectType.getRELATEDPROJECTS()) projectType.addNewRELATEDPROJECTS();

      projectDto
          .getChildren()
          .forEach(
              e ->
                  projectType
                      .getRELATEDPROJECTS()
                      .addNewRELATEDPROJECT()
                      .addNewPARENTPROJECT()
                      .setAccession(e));
    }

    transformAttributes(projectDto, projectType);

    return document;
  }

  private void convertPublicationLinksToProjectLinksAndRemovePublication(Document doc) {
    List<Node> projects = getXmlNodes(doc, "/PROJECT_SET/PROJECT");
    for (Node project : projects) {
      List<Node> xrefPublicationLinks =
          getXmlNodes(
              project, "./PUBLICATIONS/PUBLICATION/PUBLICATION_LINKS/PUBLICATION_LINK/XREF_LINK");
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
      } else {
        project.appendChild(projectLinks);
      }
    }
    return projectLinks;
  }

  private void transformIdentifiers(ProjectTransformationDTO projectDto, ProjectType projectType) {

    if (projectType.isSetIDENTIFIERS()) {
      unsetIdentifiersSubmitterIdIfBlankAlias(projectType);
    }

    // Injecting SECONDARY_ID
    if (!projectDto.getStudyIds().isEmpty())
      injectSecondaries(projectType.getIDENTIFIERS(), projectDto.getStudyIds());

    injectSecondaries(projectType.getIDENTIFIERS(), projectDto.getSecondary());

    fixIdentifiers(projectType);
  }

  private void transformLinks(ProjectTransformationDTO projectDto, ProjectType projectType) {

    retainOnlyPubmedAndURLLinks(
        projectType.getPROJECTLINKS(),
        projectType.getPROJECTLINKS() != null
            ? projectType.getPROJECTLINKS().getPROJECTLINKArray()
            : null);

    if (projectDto.getSubmissionId() != null)
      appendProjectLink(projectType, "ENA-SUBMISSION", projectDto.getSubmissionId());

    // FASTQ files
    appendProjectLink(
        projectType,
        ENA_FASTQ_FILES_TAG,
        String.format(ENA_FASTQ_FILES_URL_PREFORMAT, projectDto.getProjectId()),
        true);

    // Submission files
    appendProjectLink(
        projectType,
        ENA_SUBMITTED_FILES_TAG,
        String.format(ENA_SUBMITTED_FILES_URL_PREFORMAT, projectDto.getProjectId()),
        true);
  }

  private void transformAttributes(ProjectTransformationDTO projectDto, ProjectType projectType) {
    addFirstPublicLastUpdateAttributes(projectDto, () -> createNewAttribute(projectType));
  }

  private void appendProjectLink(ProjectType projectType, String db, String id) {
    appendProjectLink(projectType, db, id, false);
  }

  private void appendProjectLink(ProjectType projectType, String db, String id, boolean cdata) {
    appendLink(createNewLinkXRef(projectType), db, id, cdata);
  }

  private XRefType createNewLinkXRef(ProjectType projectType) {
    ProjectType.PROJECTLINKS links = projectType.getPROJECTLINKS();
    links = links == null ? projectType.addNewPROJECTLINKS() : links;

    return links.addNewPROJECTLINK().addNewXREFLINK();
  }

  private AttributeType createNewAttribute(ProjectType projectType) {
    ProjectType.PROJECTATTRIBUTES attributes = projectType.getPROJECTATTRIBUTES();
    if (attributes == null) {
      attributes = projectType.addNewPROJECTATTRIBUTES();
    }

    return attributes.addNewPROJECTATTRIBUTE();
  }
}
