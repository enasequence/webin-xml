Synopsis

This is the XML beans for the SRA XML Schemas

https://github.com/EMBL-EBI-SUBS/subs-data

http://www.ebi.ac.uk/ena

## Development,build and deployment:


* Create a branch for the new changes <br/>
    git checkout -b branch_name (typically will be a JIRA ticket id with short description)
* Update the version number in the build.gradle file.  Development versions are typically use the SNAPSHOT version suffix.  For example if the current version was 1.5.6 and your were developing a new version 1.5.7 you would use the version '1.5.7-SNAPSHOT'
* Update the relevant schema in src/main/resources/uk/ac/ebi/ena and commit it.
* Generate and commit a new test case for the schema change in src/test/java/uk/ac/ebi/ena/sra/xml/ .
* gradlew clean install ( if you are developing are testing locally)
* gradlew uploadArchives (will sign your artifacts and publish them to the sonatype snapshots area so that they are publicly available).
* Merge your changes with master
* Remove the -SNAPSHOT prefix from the build.gradle
* Tag the new version number in git
* Draft a new release with proper release notes and publish https://github.com/enasequence/schema/releases
   * More information about release can be found here: https://help.github.com/articles/creating-releases/
* gradlew uploadArchives (will upload to the sonatype staging area).
* gradlew closeAndPromoteRepository (closes and promotes the repository, it will then be available via maven central)


[Detailed information about setup, build and deployment](https://www.ebi.ac.uk/seqdb/confluence/pages/viewpage.action?spaceKey=EMBL&title=SRA+XML+Schema+Build+and+Deployment) <br/>
[For more information about release gradle tasks](https://github.com/Codearte/gradle-nexus-staging-plugin#tasks)<br/>
[For verification of the artifact release status and drop/close/release using UI](https://oss.sonatype.org/#stagingRepositories)<br/>
[To verify published version in mavenCentral](https://search.maven.org/#search%7Cga%7C1%7Csra-xml)(Note: It will take sometime to see the latest artifact in MavenCentral after closeAndRelease)<br/>




         


License

Copyright 2015 EMBL - European Bioinformatics Institute
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.