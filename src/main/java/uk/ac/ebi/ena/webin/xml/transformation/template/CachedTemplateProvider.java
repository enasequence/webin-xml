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
package uk.ac.ebi.ena.webin.xml.transformation.template;

import javax.xml.transform.Templates;
import uk.ac.ebi.ena.webin.xml.transformation.WebinXmlTransformationException;

public class CachedTemplateProvider implements TemplateProvider {

  private static final Object STUDY_MONITOR_OBJECT = new Object();
  private static final Object PROJECT_MONITOR_OBJECT = new Object();
  private static final Object SAMPLE_MONITOR_OBJECT = new Object();
  private static final Object EXPERIMENT_MONITOR_OBJECT = new Object();
  private static final Object RUN_MONITOR_OBJECT = new Object();
  private static final Object ANALYSIS_MONITOR_OBJECT = new Object();
  private static final Object EGA_DAC_MONITOR_OBJECT = new Object();
  private static final Object EGA_POLICY_MONITOR_OBJECT = new Object();
  private static final Object EGA_DATASET_MONITOR_OBJECT = new Object();
  private static final Object SUBMISSION_MONITOR_OBJECT = new Object();

  private static volatile Templates studyTemplate;
  private static volatile Templates projectTemplate;
  private static volatile Templates sampleTemplate;
  private static volatile Templates experimentTemplate;
  private static volatile Templates runTemplate;
  private static volatile Templates analysisTemplate;
  private static volatile Templates egaDacTemplate;
  private static volatile Templates egaPolicyTemplate;
  private static volatile Templates egaDatasetTemplate;
  private static volatile Templates submissionTemplate;

  private final TemplateProvider templateProvider;

  public CachedTemplateProvider(TemplateProvider templateProvider) {
    this.templateProvider = templateProvider;
  }

  @Override
  public Templates getStudyTemplate() throws WebinXmlTransformationException {
    if (studyTemplate == null) {
      synchronized (STUDY_MONITOR_OBJECT) {
        if (studyTemplate == null) {
          studyTemplate = templateProvider.getStudyTemplate();
        }
      }
    }

    return studyTemplate;
  }

  @Override
  public Templates getProjectTemplate() throws WebinXmlTransformationException {
    if (projectTemplate == null) {
      synchronized (PROJECT_MONITOR_OBJECT) {
        if (projectTemplate == null) {
          projectTemplate = templateProvider.getProjectTemplate();
        }
      }
    }

    return projectTemplate;
  }

  @Override
  public Templates getSampleTemplate() throws WebinXmlTransformationException {
    if (sampleTemplate == null) {
      synchronized (SAMPLE_MONITOR_OBJECT) {
        if (sampleTemplate == null) {
          sampleTemplate = templateProvider.getSampleTemplate();
        }
      }
    }

    return sampleTemplate;
  }

  @Override
  public Templates getExperimentTemplate() throws WebinXmlTransformationException {
    if (experimentTemplate == null) {
      synchronized (EXPERIMENT_MONITOR_OBJECT) {
        if (experimentTemplate == null) {
          experimentTemplate = templateProvider.getExperimentTemplate();
        }
      }
    }

    return experimentTemplate;
  }

  @Override
  public Templates getRunTemplate() throws WebinXmlTransformationException {
    if (runTemplate == null) {
      synchronized (RUN_MONITOR_OBJECT) {
        if (runTemplate == null) {
          runTemplate = templateProvider.getRunTemplate();
        }
      }
    }

    return runTemplate;
  }

  @Override
  public Templates getAnalysisTemplate() throws WebinXmlTransformationException {
    if (analysisTemplate == null) {
      synchronized (ANALYSIS_MONITOR_OBJECT) {
        if (analysisTemplate == null) {
          analysisTemplate = templateProvider.getAnalysisTemplate();
        }
      }
    }

    return analysisTemplate;
  }

  @Override
  public Templates getEGADacTemplate() throws WebinXmlTransformationException {
    if (egaDacTemplate == null) {
      synchronized (EGA_DAC_MONITOR_OBJECT) {
        if (egaDacTemplate == null) {
          egaDacTemplate = templateProvider.getEGADacTemplate();
        }
      }
    }

    return egaDacTemplate;
  }

  @Override
  public Templates getEGAPolicyTemplate() throws WebinXmlTransformationException {
    if (egaPolicyTemplate == null) {
      synchronized (EGA_POLICY_MONITOR_OBJECT) {
        if (egaPolicyTemplate == null) {
          egaPolicyTemplate = templateProvider.getEGAPolicyTemplate();
        }
      }
    }

    return egaPolicyTemplate;
  }

  @Override
  public Templates getEGADatasetTemplate() throws WebinXmlTransformationException {
    if (egaDatasetTemplate == null) {
      synchronized (EGA_DATASET_MONITOR_OBJECT) {
        if (egaDatasetTemplate == null) {
          egaDatasetTemplate = templateProvider.getEGADatasetTemplate();
        }
      }
    }

    return egaDatasetTemplate;
  }

  @Override
  public Templates getSubmissionTemplate() throws WebinXmlTransformationException {
    if (submissionTemplate == null) {
      synchronized (SUBMISSION_MONITOR_OBJECT) {
        if (submissionTemplate == null) {
          submissionTemplate = templateProvider.getSubmissionTemplate();
        }
      }
    }

    return submissionTemplate;
  }
}
