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
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q04E_STATUS + "=" + "http://w3id.org/bench#Q04Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q05E_STATUS + "=" + "http://w3id.org/bench#Q05Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_Q06E_STATUS + "=" + "http://w3id.org/bench#Q06Status");
        envVariablesEvaluationModule.add(GSBConstants.EVALUATION_NUMBER_OF_CORRECT_ANSWERS + "=" + "http://w3id.org/bench#totalCorrectAnswers");

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
