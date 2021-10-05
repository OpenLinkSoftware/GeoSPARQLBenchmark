package org.hobbit.geosparql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Resource;
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

    private static final Map<String,String> BenchmarkVersions=new TreeMap<>();
    
    private static final String performanceTest="http://example.org/performanceYes";

    public GSBBenchmarkController() { 
    	BenchmarkVersions.put("http://w3id.org/bench#GeoSPARQL10Compliance","geosparql10_compliance");
    	BenchmarkVersions.put("http://w3id.org/bench#GeoSPARQL10Performance","geosparql10_performance");
    	BenchmarkVersions.put("http://w3id.org/bench#GeoSPARQL11Compliance","geosparql11_compliance");
    	BenchmarkVersions.put("http://w3id.org/bench#GeoSPARQL11Performance","geosparql11_performance");
    }

    @Override
    public void init() throws Exception {
        LOGGER.info("Initialization begins.");
        super.init();

        // Your initialization code comes here...

        // You might want to load parameters from the benchmarks parameter model
        //	        NodeIterator iterator = benchmarkParamModel.listObjectsOfProperty(benchmarkParamModel
        //	                    .getProperty("http://example.org/myParameter"));

        NodeIterator iterator = benchmarkParamModel.listObjectsOfProperty(benchmarkParamModel.getProperty("http://w3id.org/bench#benchmarkVersion"));
        
        //Check the benchmark version to test
        GSBConstants configuration=null;
        if(iterator.hasNext()) {
        	try {
        		Resource res=iterator.next().asResource();
        		if(res==null) {
        			throw new Exception("Local error: Benchmark Version resource was not found!");
        		}else {
        			String uri=res.getURI();
        			if(BenchmarkVersions.containsKey(uri)) {
        				configuration=new GSBConstants(BenchmarkVersions.get(uri), BenchmarkVersions.get(uri)+".json");	
        			}   			
        		}
        	}catch(Exception e) {
        		LOGGER.error("Exception while passing parameter to benchmark",e);
        	}
        }
        if(configuration==null) {
        	configuration=new GSBConstants("geosparql10_compliance", "geosparql10_compliance.json");
        }
        
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
        for(String constant:GSBConstants.GSB_EVALUATION_STATUS.keySet()) {
        	envVariablesEvaluationModule.add(constant+"="+GSBConstants.GSB_EVALUATION_STATUS.get(constant));
        }
        
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
