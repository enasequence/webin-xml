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

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import uk.ac.ebi.ena.sra.xml.DATASETSDocument;
import uk.ac.ebi.ena.webin.xml.transformation.WebinXmlTransformationException;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.dtos.PresentationTransformationDTO;

public class EGADatasetTransformer extends AbstractTransformer
    implements Transformer<PresentationTransformationDTO, DATASETSDocument> {

  public EGADatasetTransformer(Templates transformationTemplate) {
    super(transformationTemplate);
  }

  @Override
  public Document transform(Document document) throws WebinXmlTransformationException {
    try {
      document = applyTemplateTransformation(document);

      return document;
    } catch (TransformerException e) {
      throw new WebinXmlTransformationException("Error applying EGA dataset transformation.", e);
    } catch (Exception e) {
      throw new WebinXmlTransformationException(
          "Unexpected error while transforming EGA dataset xml.", e);
    }
  }

  @Override
  public DATASETSDocument transformForPresentation(
      PresentationTransformationDTO dto, DATASETSDocument document)
      throws WebinXmlTransformationException {
    return document;
  }
}
