/*
 * Copyright 2021 EMBL - European Bioinformatics Institute
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package uk.ac.ebi.ena.webin.xml.transformation.fixers;

public class InstrumentModelEnumFixer extends EnumValueFixer {

  public InstrumentModelEnumFixer() {
    add("none", "unspecified");
    add("AB SOLiD System 3 Plus", "AB SOLiD 3 Plus System");
    add("AB SOLiD 5500xl", "AB 5500xl Genetic Analyzer");
    add("AB SOLiD 5500", "AB 5500 Genetic Analyzer");
    add("GS FLX", "454 GS FLX");
    add("GS 20", "454 GS 20");
    add("454 Titanium", "454 GS FLX Titanium");
    add("454 GS FLX Plus", "454 GS FLX+");
    add("Solexa 1G Genome Analyzer", "Illumina Genome Analyzer");
    add("Illumina NextSeq 500", "NextSeq 500");
    add("Illumina NextSeq 550", "NextSeq 550");
    add("Ion S5", "Ion Torrent S5");
    add("Ion S5 XL", "Ion Torrent S5 XL");
    add("Ion S5", "Ion Torrent S5");
    add("PacBio Sequel", "Sequel");
    add("PacBio Sequel II", "Sequel II");
  }
}
