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
package uk.ac.ebi.ena.webin.xml.conversion.json.model.submission;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;
import lombok.Data;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.Attribute;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.Identifiers;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.common.xref.Link;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.submission.action.Action;
import uk.ac.ebi.ena.webin.xml.conversion.json.utils.ActionSerializer;

@JsonPropertyOrder({
  "alias",
  "accession",
  "centerName",
  "submissionDate",
  "title",
  "identifiers",
  "actions",
  "attributes",
  "links"
})
@Data
public class Submission {
  @JacksonXmlProperty(localName = "alias", isAttribute = true)
  private String alias;

  @JacksonXmlProperty(localName = "accession", isAttribute = true)
  private String accession;

  @JacksonXmlProperty(localName = "center_name", isAttribute = true)
  private String centerName;

  @JacksonXmlProperty(localName = "submission_date", isAttribute = true)
  private String submissionDate;

  @JacksonXmlProperty(localName = "TITLE", isAttribute = true)
  private String title;

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "IDENTIFIERS")
  private Identifiers identifiers;

  /*
  ACTIONS:

  JSON representation:
  -------------------
  "actions":[
           {
              "type":"ADD"
           },
           {
              "type":"HOLD",
              "holdUntilDate":"2022-12-31"
           }
        ]

   XML representation:
   -------------------
   <ACTIONS>
        <ACTION>
          <ADD/>
        </ACTION>
        <ACTION>
          <HOLD HoldUntilDate="2022-01-01"/>
        </ACTION>
      </ACTIONS>

     */
  @JsonSerialize(using = ActionSerializer.class)
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "ACTIONS")
  // TODO ask rasko if xml to json conversion of submissions uses action serialization or not.
  private List<Action> actions;

  @JacksonXmlElementWrapper(localName = "SUBMISSION_ATTRIBUTES")
  @JacksonXmlProperty(localName = "SUBMISSION_ATTRIBUTE")
  private List<Attribute> attributes;

  @JacksonXmlElementWrapper(localName = "SUBMISSION_LINKS")
  @JacksonXmlProperty(localName = "SUBMISSION_LINK")
  private List<Link> links;
}
