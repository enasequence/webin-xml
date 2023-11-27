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
package uk.ac.ebi.ena.webin.xml.conversion.json.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import java.util.List;
import uk.ac.ebi.ena.webin.xml.conversion.json.model.submission.action.Action;

/*
Defines how each ACTION will be written in the WEBIN submission XML
 */
public class ActionSerializer extends JsonSerializer<List<Action>> {
  public ActionSerializer() {}

  @Override
  public void serialize(
      List<Action> actions, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
    ToXmlGenerator xmlGenerator;

    /*
    We are converting Java -> XML using XmlMapper(), see: WebinSubmissionPayloadSerializationService.serialize
    XmlMapper binds a ToXmlGenerator while serialization hence JsonGenerator can be safely type casted to ToXmlGenerator
    see: https://stackoverflow.com/questions/57060797/using-jackson-to-add-xml-attributes-to-manually-built-node-tree
     */
    if (jsonGenerator instanceof ToXmlGenerator) {
      try {
        xmlGenerator = (ToXmlGenerator) jsonGenerator;

        // Writing the start object which is the container of inner elements of the <ACTIONS>
        // element
        // <ACTIONS></ACTIONS> will be automatically written as the List of actions is annotated
        // with
        // @JacksonXmlProperty(localName = "ACTIONS") in Submissions.java
        xmlGenerator.writeStartObject();

        for (final Action action : actions) {
          // Writing the XML element <ACTION>
          xmlGenerator.writeFieldName("ACTION");

          // Container for all inner elements for <ACTION>
          xmlGenerator.writeStartObject();

          // Writing the action type element, <ADD>, <HOLD>, etc
          xmlGenerator.writeFieldName(action.getType().name());

          // Container for all inner elements of action type
          xmlGenerator.writeStartObject();

          // If hold until date is non-null, add it as attribute
          if (action.getHoldUntilDate() != null) {
            xmlGenerator.setNextIsAttribute(true);
            xmlGenerator.writeFieldName("HoldUntilDate");
            xmlGenerator.writeString(action.getHoldUntilDate());
            xmlGenerator.setNextIsAttribute(false);
          }

          // End the action type element
          xmlGenerator.writeEndObject();

          // End the <ACTION/> element
          xmlGenerator.writeEndObject();

          // <ACTIONS/> will be ended automatically as it annotated with
          // @JacksonXmlProperty(localName = "ACTIONS") in Submission.java
        }

        xmlGenerator.writeEndObject();
      } catch (final Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      try {
        jsonGenerator.writeStartArray();

        for (final Action action : actions) {
          jsonGenerator.writeStartObject();
          jsonGenerator.writeStringField("type", action.getType().name());

          if (action.getHoldUntilDate() != null) {
            jsonGenerator.writeStringField("holdUntilDate", action.getHoldUntilDate());
          }

          jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray();
      } catch (final Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}
