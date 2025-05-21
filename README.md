
## Development, build and deployment:

* Create a branch for the new changes `git checkout -b <branch_name>`
* Update the version number in `build.gradle`
* Update the relevant schema in the following folder and commit it `src/main/resources/uk/ac/ebi/ena`
* Generate and commit a new test case for the schema change in `src/test/java/uk/ac/ebi/ena/sra/xml/`
* `gradle clean build publishToMavenLocal` ( if you are developing and testing locally)
* Merge your changes with **master**
* Draft a new release with proper release notes and publish https://github.com/enasequence/schema/releases
   * More information about release can be found here: https://help.github.com/articles/creating-releases/
* `gradlew publish` (will upload to [EBI Gitlab](https://gitlab.ebi.ac.uk/enasequence/schema/-/packages)).

## Copying XSD files to FTP directory:

* Copy all XSD files in the directory **main/resources/uk/ac/ebi/ena/sra/schema** to `/nfs/ftp/public/databases/ena/doc/xsd/sra_1_5`
* This directory can be accessed by following these steps :
  * `ssh codon-login`
  * `become datalib`
  * `srun --partition=datamover --mem-per-cpu 64 --time 60 --pty bash`
  * `cd /nfs/ftp/public/databases/ena/doc/xsd/sra_1_5`
 
NOTE : There are multiple **sra_1_X** directories inside parent directory `/nfs/ftp/public/databases/ena/doc/xsd`. The files,
  however, are always copied into **sra_1_5** unless explicitly stated otherwise.
