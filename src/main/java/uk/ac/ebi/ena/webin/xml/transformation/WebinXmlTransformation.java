package uk.ac.ebi.ena.webin.xml.transformation;

import uk.ac.ebi.ena.webin.xml.transformation.template.CachedTemplateProvider;
import uk.ac.ebi.ena.webin.xml.transformation.template.ClasspathLoadedTemplateProvider;
import uk.ac.ebi.ena.webin.xml.transformation.template.TemplateProvider;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.AnalysisTransformer;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.EGADacTransformer;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.EGADatasetTransformer;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.EGAPolicyTransformer;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.ExperimentTransformer;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.ProjectTransformer;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.RunTransformer;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.SampleTransformer;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.StudyTransformer;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.SubmissionTransformer;
import uk.ac.ebi.ena.webin.xml.transformation.transformers.Transformer;

/**
 * Creates and returns transformer for every object type. The returned transformer objects are thread-safe.
 */
public class WebinXmlTransformation {

    private static String STUDY_TEMPLATE_CLASSPATH_URL = "/uk/ac/ebi/ena/webin/xml/transformation/xslt/study.xslt";
    private static String PROJECT_TEMPLATE_CLASSPATH_URL = "/uk/ac/ebi/ena/webin/xml/transformation/xslt/project.xslt";
    private static String SAMPLE_TEMPLATE_CLASSPATH_URL = "/uk/ac/ebi/ena/webin/xml/transformation/xslt/sample.xslt";
    private static String EXPERIMENT_TEMPLATE_CLASSPATH_URL = "/uk/ac/ebi/ena/webin/xml/transformation/xslt/experiment.xslt";
    private static String RUN_TEMPLATE_CLASSPATH_URL = "/uk/ac/ebi/ena/webin/xml/transformation/xslt/run.xslt";
    private static String ANALYSIS_TEMPLATE_CLASSPATH_URL = "/uk/ac/ebi/ena/webin/xml/transformation/xslt/analysis.xslt";
    private static String EGA_DAC_TEMPLATE_CLASSPATH_URL = "/uk/ac/ebi/ena/webin/xml/transformation/xslt/ega-dac.xslt";
    private static String EGA_POLICY_TEMPLATE_CLASSPATH_URL = "/uk/ac/ebi/ena/webin/xml/transformation/xslt/ega-policy.xslt";
    private static String EGA_DATASET_TEMPLATE_CLASSPATH_URL = "/uk/ac/ebi/ena/webin/xml/transformation/xslt/ega-dataset.xslt";
    private static String SUBMISSION_TEMPLATE_CLASSPATH_URL = "/uk/ac/ebi/ena/webin/xml/transformation/xslt/submission.xslt";

    private static TemplateProvider templateProvider = new CachedTemplateProvider(new ClasspathLoadedTemplateProvider(
        STUDY_TEMPLATE_CLASSPATH_URL,
        PROJECT_TEMPLATE_CLASSPATH_URL,
        SAMPLE_TEMPLATE_CLASSPATH_URL,
        EXPERIMENT_TEMPLATE_CLASSPATH_URL,
        RUN_TEMPLATE_CLASSPATH_URL,
        ANALYSIS_TEMPLATE_CLASSPATH_URL,
        EGA_DAC_TEMPLATE_CLASSPATH_URL,
        EGA_POLICY_TEMPLATE_CLASSPATH_URL,
        EGA_DATASET_TEMPLATE_CLASSPATH_URL,
        SUBMISSION_TEMPLATE_CLASSPATH_URL
    ));

    public static Transformer createStudyTransformer() throws WebinXmlTransformationException {
        return new StudyTransformer(templateProvider.getStudyTemplate());
    }

    public static Transformer createProjectTransformer() throws WebinXmlTransformationException {
        return new ProjectTransformer(templateProvider.getProjectTemplate());
    }

    public static Transformer createSampleTransformer() throws WebinXmlTransformationException {
        return new SampleTransformer(templateProvider.getSampleTemplate());
    }

    public static Transformer createExperimentTransformer() throws WebinXmlTransformationException {
        return new ExperimentTransformer(templateProvider.getExperimentTemplate());
    }

    public static Transformer createRunTransformer() throws WebinXmlTransformationException {
        return new RunTransformer(templateProvider.getRunTemplate());
    }

    public static Transformer createAnalysisTransformer() throws WebinXmlTransformationException {
        return new AnalysisTransformer(templateProvider.getAnalysisTemplate());
    }

    public static Transformer createEGADacTransformer() throws WebinXmlTransformationException {
        return new EGADacTransformer(templateProvider.getEGADacTemplate());
    }

    public static Transformer createEGAPolicyTransformer() throws WebinXmlTransformationException {
        return new EGAPolicyTransformer(templateProvider.getEGAPolicyTemplate());
    }

    public static Transformer createEGADatasetTransformer() throws WebinXmlTransformationException {
        return new EGADatasetTransformer(templateProvider.getEGADatasetTemplate());
    }

    public static Transformer createSubmissionTransformer() throws WebinXmlTransformationException {
        return new SubmissionTransformer(templateProvider.getSubmissionTemplate());
    }
}
