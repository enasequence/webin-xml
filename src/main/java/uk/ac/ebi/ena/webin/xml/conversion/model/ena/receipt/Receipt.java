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
package uk.ac.ebi.ena.webin.xml.conversion.model.ena.receipt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;
import lombok.Data;
import uk.ac.ebi.ena.webin.xml.conversion.model.ena.receipt.message.Messages;

@JacksonXmlRootElement(localName = "RECEIPT")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
  "success",
  "receiptDate",
  "submissionFile",
  "analyses",
  "experiments",
  "runs",
  "samples",
  "studies",
  "projects",
  "submission",
  "messages",
  "actions"
})
@Data
public class Receipt {
  @JacksonXmlProperty(localName = "success", isAttribute = true)
  private boolean success;

  @JacksonXmlProperty(localName = "receiptDate", isAttribute = true)
  private String receiptDate;

  @JacksonXmlElementWrapper(localName = "ANALYSIS", useWrapping = false)
  @JacksonXmlProperty(localName = "ANALYSIS")
  private List<ReceiptObject> analyses;

  @JacksonXmlElementWrapper(localName = "EXPERIMENT", useWrapping = false)
  @JacksonXmlProperty(localName = "EXPERIMENT")
  private List<ReceiptObject> experiments;

  @JacksonXmlElementWrapper(localName = "RUN", useWrapping = false)
  @JacksonXmlProperty(localName = "RUN")
  private List<ReceiptObject> runs;

  @JacksonXmlElementWrapper(localName = "SAMPLE", useWrapping = false)
  @JacksonXmlProperty(localName = "SAMPLE")
  private List<ReceiptObject> samples;

  @JacksonXmlElementWrapper(localName = "STUDY", useWrapping = false)
  @JacksonXmlProperty(localName = "STUDY")
  private List<ReceiptObject> studies;

  @JacksonXmlElementWrapper(localName = "PROJECT", useWrapping = false)
  @JacksonXmlProperty(localName = "PROJECT")
  private List<ReceiptObject> projects;

  @JacksonXmlProperty(localName = "SUBMISSION")
  private ReceiptObject submission;

  @JacksonXmlProperty(localName = "MESSAGES")
  @JacksonXmlElementWrapper(useWrapping = false)
  private Messages messages;

  @JacksonXmlProperty(localName = "ACTIONS")
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<String> actions;
}
