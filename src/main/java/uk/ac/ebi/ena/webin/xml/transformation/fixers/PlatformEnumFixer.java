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
package uk.ac.ebi.ena.webin.xml.transformation.fixers;

import org.apache.xmlbeans.SchemaStringEnumEntry;
import uk.ac.ebi.ena.sra.xml.Type454Model;
import uk.ac.ebi.ena.sra.xml.TypeAbiSolidModel;
import uk.ac.ebi.ena.sra.xml.TypeBGISEQModel;
import uk.ac.ebi.ena.sra.xml.TypeCGModel;
import uk.ac.ebi.ena.sra.xml.TypeCapillaryModel;
import uk.ac.ebi.ena.sra.xml.TypeDnbSeqModel;
import uk.ac.ebi.ena.sra.xml.TypeElementModel;
import uk.ac.ebi.ena.sra.xml.TypeGenapsysModel;
import uk.ac.ebi.ena.sra.xml.TypeGeneMindModel;
import uk.ac.ebi.ena.sra.xml.TypeHelicosModel;
import uk.ac.ebi.ena.sra.xml.TypeIlluminaModel;
import uk.ac.ebi.ena.sra.xml.TypeIontorrentModel;
import uk.ac.ebi.ena.sra.xml.TypeOxfordNanoporeModel;
import uk.ac.ebi.ena.sra.xml.TypePacBioModel;
import uk.ac.ebi.ena.sra.xml.TypeTapestriModel;
import uk.ac.ebi.ena.sra.xml.TypeUltimaModel;
import uk.ac.ebi.ena.sra.xml.TypeVelaDiagnosticsModel;

public class PlatformEnumFixer extends EnumValueFixer {
  public PlatformEnumFixer() {
    /** Any addition or removal of models in SRA.common.xsd should be reflected here. */
    addValueList(Type454Model.Factory.newInstance().schemaType().getStringEnumEntries(), "LS454");
    addValueList(
        TypeIlluminaModel.Factory.newInstance().schemaType().getStringEnumEntries(), "ILLUMINA");
    addValueList(
        TypeHelicosModel.Factory.newInstance().schemaType().getStringEnumEntries(), "HELICOS");
    addValueList(
        TypeAbiSolidModel.Factory.newInstance().schemaType().getStringEnumEntries(), "ABI_SOLID");
    addValueList(
        TypeCGModel.Factory.newInstance().schemaType().getStringEnumEntries(), "COMPLETE_GENOMICS");
    addValueList(
        TypeBGISEQModel.Factory.newInstance().schemaType().getStringEnumEntries(), "BGISEQ");
    addValueList(
        TypePacBioModel.Factory.newInstance().schemaType().getStringEnumEntries(), "PACBIO_SMRT");
    addValueList(
        TypeIontorrentModel.Factory.newInstance().schemaType().getStringEnumEntries(),
        "ION_TORRENT");
    addValueList(
        TypeCapillaryModel.Factory.newInstance().schemaType().getStringEnumEntries(), "CAPILLARY");
    addValueList(
        TypeDnbSeqModel.Factory.newInstance().schemaType().getStringEnumEntries(), "DNBSEQ");
    addValueList(
        TypeOxfordNanoporeModel.Factory.newInstance().schemaType().getStringEnumEntries(),
        "OXFORD_NANOPORE");
    addValueList(
        TypeElementModel.Factory.newInstance().schemaType().getStringEnumEntries(), "ELEMENT");
    addValueList(
        TypeUltimaModel.Factory.newInstance().schemaType().getStringEnumEntries(), "ULTIMA");
    addValueList(
        TypeVelaDiagnosticsModel.Factory.newInstance().schemaType().getStringEnumEntries(),
        "VELA_DIAGNOSTICS");
    addValueList(
        TypeGenapsysModel.Factory.newInstance().schemaType().getStringEnumEntries(), "GENAPSYS");
    addValueList(
        TypeGeneMindModel.Factory.newInstance().schemaType().getStringEnumEntries(), "GENEMIND");
    addValueList(
        TypeTapestriModel.Factory.newInstance().schemaType().getStringEnumEntries(), "TAPESTRI");
  }

  private void addValueList(SchemaStringEnumEntry[] keys, String value) {
    for (SchemaStringEnumEntry key : keys) {
      if (!key.getString().equalsIgnoreCase("unspecified")) {
        add(key.getString(), value);
      }
    }
  }
}
