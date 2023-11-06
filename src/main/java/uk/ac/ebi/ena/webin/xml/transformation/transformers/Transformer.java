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

import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Document;
import uk.ac.ebi.ena.webin.xml.transformation.WebinXmlTransformationException;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.dtos.PresentationTransformationDTO;

public interface Transformer<T extends PresentationTransformationDTO, U extends XmlObject> {

  Document transform(Document document) throws WebinXmlTransformationException;

  U transformForPresentation(T transformationDTO, U objDocument)
      throws WebinXmlTransformationException;
}
