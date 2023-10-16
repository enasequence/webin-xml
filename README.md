Synopsis

This is the XML beans for the SRA XML Schemas

https://github.com/EMBL-EBI-SUBS/subs-data

http://www.ebi.ac.uk/ena

## Development, build and deployment:

* Create a branch for the new changes `git checkout -b <branch_name>`
* Update the version number in `build.gradle`
* Update the relevant schema in the following folder and commit it `src/main/resources/uk/ac/ebi/ena`
* Generate and commit a new test case for the schema change in `src/test/java/uk/ac/ebi/ena/sra/xml/`
* `gradlew clean install` ( if you are developing and testing locally)
* Merge your changes with **master**
* Tag the new version number in git
* Draft a new release with proper release notes and publish https://github.com/enasequence/schema/releases
   * More information about release can be found here: https://help.github.com/articles/creating-releases/
* `gradlew publish` (will upload to [EBI Gitlab](https://gitlab.ebi.ac.uk/enasequence/schema/-/packages)).

## Copying files to FTP directory:

* Copy all XSD files in the directory **main/resources/uk/ac/ebi/ena/sra/schema** to `/nfs/ftp/pub/databases/ena/doc/xsd/sra_1_5`
* This directory can be accessed :
  * `$ ssh codon-login`
  * `$ bsub -Is -q datamover bash`
  * `$ become datalib`
  * `$ cd /nfs/ftp/pub/databases/ena/doc/xsd/sra_1_5`
 
NOTE : There are multiple **sra_1_X** directories inside parent directory `/nfs/ftp/pub/databases/ena/doc/xsd`. The files,
  however, are always copied into **sra_1_5** unless explicitly stated otherwise.
