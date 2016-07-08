Synopsis

This is the main project for the SRA core.

It supports the loading or SRA XML metadata into the database.  It is used by both the RESTful SRA loader project (era-srv) and SRA Webin (webin-sra-gwt).

[![Build Status](http://ves-ebi-de:8080/buildStatus/icon?job=sra)](http://ves-ebi-de:8080/job/sra/)

## Build and Deployments

[See Confluence page](http://www.ebi.ac.uk/seqdb/confluence/display/EMBL/SRA+API+build+instructions)

## Building and running the tests locally

./gradlew -Penv=localtest build

## Publishing to the local maven repository

./gradlew -Penv=localtest install

## Publishing the jar to the remote area used by the SRA flow manager jobs

./gradlew -Penv=localtest deployFatJar

## Contributors

Neil Goodgame
Rajesh Rallan

License

Copyright 2015 EMBL - European Bioinformatics Institute
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.