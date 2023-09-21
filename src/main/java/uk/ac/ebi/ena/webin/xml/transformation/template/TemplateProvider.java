package uk.ac.ebi.ena.webin.xml.transformation.template;

import uk.ac.ebi.ena.webin.xml.transformation.WebinXmlTransformationException;

import javax.xml.transform.Templates;

public interface TemplateProvider {

    Templates getStudyTemplate() throws WebinXmlTransformationException;
    Templates getProjectTemplate() throws WebinXmlTransformationException;
    Templates getSampleTemplate() throws WebinXmlTransformationException;
    Templates getExperimentTemplate() throws WebinXmlTransformationException;
    Templates getRunTemplate() throws WebinXmlTransformationException;
    Templates getAnalysisTemplate() throws WebinXmlTransformationException;
    Templates getEGADacTemplate() throws WebinXmlTransformationException;
    Templates getEGAPolicyTemplate() throws WebinXmlTransformationException;
    Templates getEGADatasetTemplate() throws WebinXmlTransformationException;
    Templates getSubmissionTemplate() throws WebinXmlTransformationException;
}
