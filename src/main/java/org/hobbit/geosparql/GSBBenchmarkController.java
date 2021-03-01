package org.hobbit.geosparql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.hobbit.core.Commands;
import org.hobbit.core.Constants;
import org.hobbit.core.components.AbstractBenchmarkController;
import org.hobbit.geosparql.util.GSBConstants;
import org.hobbit.geosparql.util.VirtuosoSystemAdapterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GSBBenchmarkController extends AbstractBenchmarkController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GSBBenchmarkController.class);
    private ArrayList<String> envVariablesEvaluationModule = new ArrayList<String>();; 
    private long loadingStarted = -1;
    private long loadingEnded;

    // TODO: Add image names of containers
    /* Data generator Docker image */
    private static final String DATA_GENERATOR_CONTAINER_IMAGE = "git.project-hobbit.eu:4567/mjovanovik/gsb-datagenerator";
    /* Task generator Docker image */ 
    private static final String SEQ_TASK_GENERATOR_CONTAINER_IMAGE = "git.project-hobbit.eu:4567/mjovanovik/gsb-seqtaskgenerator";
    /* Evaluation module Docker image */
    private static final String EVALUATION_MODULE_CONTAINER_IMAGE = "git.project-hobbit.eu:4567/mjovanovik/gsb-evaluationmodule";

    public GSBBenchmarkController() { 
    
    }

    @Override
    public void init() throws Exception {
        LOGGER.info("Initialization begins.");
        super.init();

        // Your initialization code comes here...

        // You might want to load parameters from the benchmarks parameter model
        //	        NodeIterator iterator = benchmarkParamModel.listObjectsOfProperty(benchmarkParamModel
        //	                    .getProperty("http://example.org/myParameter"));

        // Create data generators
        int numberOfDataGenerators = 1;
        String[] envVariables = new String[]{};
        createDataGenerators(DATA_GENERATOR_CONTAINER_IMAGE, numberOfDataGenerators, envVariables);

        // Create task generators
        int numberOfTaskGenerators = 1;
        envVariables = new String[] {};
        createTaskGenerators(SEQ_TASK_GENERATOR_CONTAINER_IMAGE, numberOfTaskGenerators, envVariables);

        // Create evaluation storage
        envVariables = ArrayUtils.add(DEFAULT_EVAL_STORAGE_PARAMETERS,
        Constants.RABBIT_MQ_HOST_NAME_KEY + "=" + this.rabbitMQHostName);
        envVariables = ArrayUtils.add(envVariables, "ACKNOWLEDGEMENT_FLAG=true");
        createEvaluationStorage(DEFAULT_EVAL_STORAGE_IMAGE, envVariables);

        // KPIs for evaluation module
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q01E_STATUS + "=" + "http://w3id.org/bench#Q01Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q02E_STATUS + "=" + "http://w3id.org/bench#Q02Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q03E_STATUS + "=" + "http://w3id.org/bench#Q03Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q04_1E_STATUS + "=" + "http://w3id.org/bench#Q04_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q04_2E_STATUS + "=" + "http://w3id.org/bench#Q04_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q04_3E_STATUS + "=" + "http://w3id.org/bench#Q04_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q04_4E_STATUS + "=" + "http://w3id.org/bench#Q04_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q04_5E_STATUS + "=" + "http://w3id.org/bench#Q04_5Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q04_6E_STATUS + "=" + "http://w3id.org/bench#Q04_6Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q04_7E_STATUS + "=" + "http://w3id.org/bench#Q04_7Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q04_8E_STATUS + "=" + "http://w3id.org/bench#Q04_8Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q05_1E_STATUS + "=" + "http://w3id.org/bench#Q05_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q05_2E_STATUS + "=" + "http://w3id.org/bench#Q05_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q05_3E_STATUS + "=" + "http://w3id.org/bench#Q05_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q05_4E_STATUS + "=" + "http://w3id.org/bench#Q05_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q05_5E_STATUS + "=" + "http://w3id.org/bench#Q05_5Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q05_6E_STATUS + "=" + "http://w3id.org/bench#Q05_6Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q05_7E_STATUS + "=" + "http://w3id.org/bench#Q05_7Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q05_8E_STATUS + "=" + "http://w3id.org/bench#Q05_8Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q06_1E_STATUS + "=" + "http://w3id.org/bench#Q06_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q06_2E_STATUS + "=" + "http://w3id.org/bench#Q06_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q06_3E_STATUS + "=" + "http://w3id.org/bench#Q06_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q06_4E_STATUS + "=" + "http://w3id.org/bench#Q06_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q06_5E_STATUS + "=" + "http://w3id.org/bench#Q06_5Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q06_6E_STATUS + "=" + "http://w3id.org/bench#Q06_6Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q06_7E_STATUS + "=" + "http://w3id.org/bench#Q06_7Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q06_8E_STATUS + "=" + "http://w3id.org/bench#Q06_8Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q07E_STATUS + "=" + "http://w3id.org/bench#Q07Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q08_1E_STATUS + "=" + "http://w3id.org/bench#Q08_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q08_2E_STATUS + "=" + "http://w3id.org/bench#Q08_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q09_1E_STATUS + "=" + "http://w3id.org/bench#Q09_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q09_2E_STATUS + "=" + "http://w3id.org/bench#Q09_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q09_3E_STATUS + "=" + "http://w3id.org/bench#Q09_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q09_4E_STATUS + "=" + "http://w3id.org/bench#Q09_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q09_5E_STATUS + "=" + "http://w3id.org/bench#Q09_5Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q09_6E_STATUS + "=" + "http://w3id.org/bench#Q09_6Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q10E_STATUS + "=" + "http://w3id.org/bench#Q10Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q11E_STATUS + "=" + "http://w3id.org/bench#Q11Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q12E_STATUS + "=" + "http://w3id.org/bench#Q12Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q13_1E_STATUS + "=" + "http://w3id.org/bench#Q13_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q13_2E_STATUS + "=" + "http://w3id.org/bench#Q13_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q14E_STATUS + "=" + "http://w3id.org/bench#Q14Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q15E_STATUS + "=" + "http://w3id.org/bench#Q15Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q16_1E_STATUS + "=" + "http://w3id.org/bench#Q16_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q16_2E_STATUS + "=" + "http://w3id.org/bench#Q16_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q18E_STATUS + "=" + "http://w3id.org/bench#Q18Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_1_1E_STATUS + "=" + "http://w3id.org/bench#Q19_1_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_1_2E_STATUS + "=" + "http://w3id.org/bench#Q19_1_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_1_3E_STATUS + "=" + "http://w3id.org/bench#Q19_1_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_1_4E_STATUS + "=" + "http://w3id.org/bench#Q19_1_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_2_1E_STATUS + "=" + "http://w3id.org/bench#Q19_2_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_2_2E_STATUS + "=" + "http://w3id.org/bench#Q19_2_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_3_1E_STATUS + "=" + "http://w3id.org/bench#Q19_3_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_3_2E_STATUS + "=" + "http://w3id.org/bench#Q19_3_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_4_1E_STATUS + "=" + "http://w3id.org/bench#Q19_4_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_4_2E_STATUS + "=" + "http://w3id.org/bench#Q19_4_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_4_3E_STATUS + "=" + "http://w3id.org/bench#Q19_4_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_4_4E_STATUS + "=" + "http://w3id.org/bench#Q19_4_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_5_1E_STATUS + "=" + "http://w3id.org/bench#Q19_5_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_5_2E_STATUS + "=" + "http://w3id.org/bench#Q19_5_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_5_3E_STATUS + "=" + "http://w3id.org/bench#Q19_5_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_5_4E_STATUS + "=" + "http://w3id.org/bench#Q19_5_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_6_1E_STATUS + "=" + "http://w3id.org/bench#Q19_6_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_6_2E_STATUS + "=" + "http://w3id.org/bench#Q19_6_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_6_3E_STATUS + "=" + "http://w3id.org/bench#Q19_6_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_6_4E_STATUS + "=" + "http://w3id.org/bench#Q19_6_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_7_1E_STATUS + "=" + "http://w3id.org/bench#Q19_7_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_7_2E_STATUS + "=" + "http://w3id.org/bench#Q19_7_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_7_3E_STATUS + "=" + "http://w3id.org/bench#Q19_7_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_7_4E_STATUS + "=" + "http://w3id.org/bench#Q19_7_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_8_1E_STATUS + "=" + "http://w3id.org/bench#Q19_8_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_8_2E_STATUS + "=" + "http://w3id.org/bench#Q19_8_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_9_1E_STATUS + "=" + "http://w3id.org/bench#Q19_9_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q19_9_2E_STATUS + "=" + "http://w3id.org/bench#Q19_9_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q20_1E_STATUS + "=" + "http://w3id.org/bench#Q20_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q20_2E_STATUS + "=" + "http://w3id.org/bench#Q20_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q21_1E_STATUS + "=" + "http://w3id.org/bench#Q21_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q21_2E_STATUS + "=" + "http://w3id.org/bench#Q21_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q21_3E_STATUS + "=" + "http://w3id.org/bench#Q21_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q21_4E_STATUS + "=" + "http://w3id.org/bench#Q21_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_1_1E_STATUS + "=" + "http://w3id.org/bench#Q22_1_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_1_2E_STATUS + "=" + "http://w3id.org/bench#Q22_1_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_1_3E_STATUS + "=" + "http://w3id.org/bench#Q22_1_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_1_4E_STATUS + "=" + "http://w3id.org/bench#Q22_1_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_2_1E_STATUS + "=" + "http://w3id.org/bench#Q22_2_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_2_2E_STATUS + "=" + "http://w3id.org/bench#Q22_2_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_2_3E_STATUS + "=" + "http://w3id.org/bench#Q22_2_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_2_4E_STATUS + "=" + "http://w3id.org/bench#Q22_2_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_3_1E_STATUS + "=" + "http://w3id.org/bench#Q22_3_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_3_2E_STATUS + "=" + "http://w3id.org/bench#Q22_3_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_3_3E_STATUS + "=" + "http://w3id.org/bench#Q22_3_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_3_4E_STATUS + "=" + "http://w3id.org/bench#Q22_3_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_4_1E_STATUS + "=" + "http://w3id.org/bench#Q22_4_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_4_2E_STATUS + "=" + "http://w3id.org/bench#Q22_4_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_4_3E_STATUS + "=" + "http://w3id.org/bench#Q22_4_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_4_4E_STATUS + "=" + "http://w3id.org/bench#Q22_4_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_5_1E_STATUS + "=" + "http://w3id.org/bench#Q22_5_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_5_2E_STATUS + "=" + "http://w3id.org/bench#Q22_5_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_5_3E_STATUS + "=" + "http://w3id.org/bench#Q22_5_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_5_4E_STATUS + "=" + "http://w3id.org/bench#Q22_5_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_6_1E_STATUS + "=" + "http://w3id.org/bench#Q22_6_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_6_2E_STATUS + "=" + "http://w3id.org/bench#Q22_6_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_6_3E_STATUS + "=" + "http://w3id.org/bench#Q22_6_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_6_4E_STATUS + "=" + "http://w3id.org/bench#Q22_6_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_7_1E_STATUS + "=" + "http://w3id.org/bench#Q22_7_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_7_2E_STATUS + "=" + "http://w3id.org/bench#Q22_7_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_7_3E_STATUS + "=" + "http://w3id.org/bench#Q22_7_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_7_4E_STATUS + "=" + "http://w3id.org/bench#Q22_7_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_8_1E_STATUS + "=" + "http://w3id.org/bench#Q22_8_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_8_2E_STATUS + "=" + "http://w3id.org/bench#Q22_8_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_8_3E_STATUS + "=" + "http://w3id.org/bench#Q22_8_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q22_8_4E_STATUS + "=" + "http://w3id.org/bench#Q22_8_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_1_1E_STATUS + "=" + "http://w3id.org/bench#Q23_1_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_1_2E_STATUS + "=" + "http://w3id.org/bench#Q23_1_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_1_3E_STATUS + "=" + "http://w3id.org/bench#Q23_1_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_1_4E_STATUS + "=" + "http://w3id.org/bench#Q23_1_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_2_1E_STATUS + "=" + "http://w3id.org/bench#Q23_2_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_2_2E_STATUS + "=" + "http://w3id.org/bench#Q23_2_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_2_3E_STATUS + "=" + "http://w3id.org/bench#Q23_2_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_2_4E_STATUS + "=" + "http://w3id.org/bench#Q23_2_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_3_1E_STATUS + "=" + "http://w3id.org/bench#Q23_3_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_3_2E_STATUS + "=" + "http://w3id.org/bench#Q23_3_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_3_3E_STATUS + "=" + "http://w3id.org/bench#Q23_3_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_3_4E_STATUS + "=" + "http://w3id.org/bench#Q23_3_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_4_1E_STATUS + "=" + "http://w3id.org/bench#Q23_4_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_4_2E_STATUS + "=" + "http://w3id.org/bench#Q23_4_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_4_3E_STATUS + "=" + "http://w3id.org/bench#Q23_4_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_4_4E_STATUS + "=" + "http://w3id.org/bench#Q23_4_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_5_1E_STATUS + "=" + "http://w3id.org/bench#Q23_5_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_5_2E_STATUS + "=" + "http://w3id.org/bench#Q23_5_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_5_3E_STATUS + "=" + "http://w3id.org/bench#Q23_5_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_5_4E_STATUS + "=" + "http://w3id.org/bench#Q23_5_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_6_1E_STATUS + "=" + "http://w3id.org/bench#Q23_6_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_6_2E_STATUS + "=" + "http://w3id.org/bench#Q23_6_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_6_3E_STATUS + "=" + "http://w3id.org/bench#Q23_6_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_6_4E_STATUS + "=" + "http://w3id.org/bench#Q23_6_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_7_1E_STATUS + "=" + "http://w3id.org/bench#Q23_7_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_7_2E_STATUS + "=" + "http://w3id.org/bench#Q23_7_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_7_3E_STATUS + "=" + "http://w3id.org/bench#Q23_7_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_7_4E_STATUS + "=" + "http://w3id.org/bench#Q23_7_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_8_1E_STATUS + "=" + "http://w3id.org/bench#Q23_8_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_8_2E_STATUS + "=" + "http://w3id.org/bench#Q23_8_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_8_3E_STATUS + "=" + "http://w3id.org/bench#Q23_8_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q23_8_4E_STATUS + "=" + "http://w3id.org/bench#Q23_8_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_1_1E_STATUS + "=" + "http://w3id.org/bench#Q24_1_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_1_2E_STATUS + "=" + "http://w3id.org/bench#Q24_1_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_1_3E_STATUS + "=" + "http://w3id.org/bench#Q24_1_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_1_4E_STATUS + "=" + "http://w3id.org/bench#Q24_1_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_2_1E_STATUS + "=" + "http://w3id.org/bench#Q24_2_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_2_2E_STATUS + "=" + "http://w3id.org/bench#Q24_2_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_2_3E_STATUS + "=" + "http://w3id.org/bench#Q24_2_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_2_4E_STATUS + "=" + "http://w3id.org/bench#Q24_2_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_3_1E_STATUS + "=" + "http://w3id.org/bench#Q24_3_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_3_2E_STATUS + "=" + "http://w3id.org/bench#Q24_3_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_3_3E_STATUS + "=" + "http://w3id.org/bench#Q24_3_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_3_4E_STATUS + "=" + "http://w3id.org/bench#Q24_3_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_4_1E_STATUS + "=" + "http://w3id.org/bench#Q24_4_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_4_2E_STATUS + "=" + "http://w3id.org/bench#Q24_4_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_4_3E_STATUS + "=" + "http://w3id.org/bench#Q24_4_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_4_4E_STATUS + "=" + "http://w3id.org/bench#Q24_4_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_5_1E_STATUS + "=" + "http://w3id.org/bench#Q24_5_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_5_2E_STATUS + "=" + "http://w3id.org/bench#Q24_5_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_5_3E_STATUS + "=" + "http://w3id.org/bench#Q24_5_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_5_4E_STATUS + "=" + "http://w3id.org/bench#Q24_5_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_6_1E_STATUS + "=" + "http://w3id.org/bench#Q24_6_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_6_2E_STATUS + "=" + "http://w3id.org/bench#Q24_6_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_6_3E_STATUS + "=" + "http://w3id.org/bench#Q24_6_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_6_4E_STATUS + "=" + "http://w3id.org/bench#Q24_6_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_7_1E_STATUS + "=" + "http://w3id.org/bench#Q24_7_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_7_2E_STATUS + "=" + "http://w3id.org/bench#Q24_7_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_7_3E_STATUS + "=" + "http://w3id.org/bench#Q24_7_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_7_4E_STATUS + "=" + "http://w3id.org/bench#Q24_7_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_8_1E_STATUS + "=" + "http://w3id.org/bench#Q24_8_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_8_2E_STATUS + "=" + "http://w3id.org/bench#Q24_8_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_8_3E_STATUS + "=" + "http://w3id.org/bench#Q24_8_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q24_8_4E_STATUS + "=" + "http://w3id.org/bench#Q24_8_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q25_1E_STATUS + "=" + "http://w3id.org/bench#Q25_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q25_2E_STATUS + "=" + "http://w3id.org/bench#Q25_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q25_3E_STATUS + "=" + "http://w3id.org/bench#Q25_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q26_1E_STATUS + "=" + "http://w3id.org/bench#Q26_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q26_2E_STATUS + "=" + "http://w3id.org/bench#Q26_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q27E_STATUS + "=" + "http://w3id.org/bench#Q27Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q28_1E_STATUS + "=" + "http://w3id.org/bench#Q28_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q28_2E_STATUS + "=" + "http://w3id.org/bench#Q28_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q28_3E_STATUS + "=" + "http://w3id.org/bench#Q28_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q28_4E_STATUS + "=" + "http://w3id.org/bench#Q28_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q28_5E_STATUS + "=" + "http://w3id.org/bench#Q28_5Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q28_6E_STATUS + "=" + "http://w3id.org/bench#Q28_6Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q28_7E_STATUS + "=" + "http://w3id.org/bench#Q28_7Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q28_8E_STATUS + "=" + "http://w3id.org/bench#Q28_8Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q29_1E_STATUS + "=" + "http://w3id.org/bench#Q29_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q29_2E_STATUS + "=" + "http://w3id.org/bench#Q29_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q29_3E_STATUS + "=" + "http://w3id.org/bench#Q29_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q29_4E_STATUS + "=" + "http://w3id.org/bench#Q29_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q29_5E_STATUS + "=" + "http://w3id.org/bench#Q29_5Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q29_6E_STATUS + "=" + "http://w3id.org/bench#Q29_6Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q29_7E_STATUS + "=" + "http://w3id.org/bench#Q29_7Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q29_8E_STATUS + "=" + "http://w3id.org/bench#Q29_8Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q30_1E_STATUS + "=" + "http://w3id.org/bench#Q30_1Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q30_2E_STATUS + "=" + "http://w3id.org/bench#Q30_2Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q30_3E_STATUS + "=" + "http://w3id.org/bench#Q30_3Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q30_4E_STATUS + "=" + "http://w3id.org/bench#Q30_4Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q30_5E_STATUS + "=" + "http://w3id.org/bench#Q30_5Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q30_6E_STATUS + "=" + "http://w3id.org/bench#Q30_6Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q30_7E_STATUS + "=" + "http://w3id.org/bench#Q30_7Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q30_8E_STATUS + "=" + "http://w3id.org/bench#Q30_8Status");

        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_NUMBER_OF_CORRECT_ANSWERS + "=" + "http://w3id.org/bench#totalCorrectAnswers");

        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_PERCENTAGE_OF_SATISFIED_REQUIREMENTS + "=" + "http://w3id.org/bench#percentageCorrectAnswers");

        // Wait for all components to finish their initialization
        waitForComponentsToInitialize();

        LOGGER.info("Initialization is over.");
    }

    @Override
    protected void executeBenchmark() throws Exception {
        LOGGER.info("Executing benchmark has started.");

        // give the start signals
        LOGGER.info("Send start signal to Data and Task Generators.");
        sendToCmdQueue(Commands.TASK_GENERATOR_START_SIGNAL);
        sendToCmdQueue(Commands.DATA_GENERATOR_START_SIGNAL);

        // wait for the data generators to finish their work
        waitForDataGenToFinish();
        // wait for the task generators to finish their work
        waitForTaskGenToFinish();
        // wait for the system to terminate
        waitForSystemToFinish();

        LOGGER.info("Evaluation in progress...");
        // envVariablesEvaluationModule.add(GSBConstants.EVALUATION_REAL_LOADING_TIME + "=" + (loadingEnded - loadingStarted));
        createEvaluationModule(EVALUATION_MODULE_CONTAINER_IMAGE, envVariablesEvaluationModule.toArray(new String[0]));

        // wait for the evaluation to finish
        waitForEvalComponentsToFinish();

        sendResultModel(this.resultModel);

        LOGGER.info("Executing benchmark is over.");
    }
	
    @Override
    public void receiveCommand(byte command, byte[] data) {
    	if (VirtuosoSystemAdapterConstants.BULK_LOAD_DATA_GEN_FINISHED_FROM_DATAGEN == command) {
            loadingStarted = System.currentTimeMillis();
            try {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                sendToCmdQueue(VirtuosoSystemAdapterConstants.BULK_LOAD_DATA_GEN_FINISHED, data);
            } catch (IOException e) {
                e.printStackTrace();
            }
    	}
    	else if (command == VirtuosoSystemAdapterConstants.BULK_LOADING_DATA_FINISHED) {
            loadingEnded = System.currentTimeMillis();
    	}
    	super.receiveCommand(command, data);	
    }
}
