package org.hobbit.geosparql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.hobbit.core.Constants;
import org.hobbit.core.components.AbstractEvaluationModule;
import org.hobbit.core.rabbit.RabbitMQUtils;
import org.hobbit.geosparql.util.GSBConstants;
import org.hobbit.vocab.HOBBIT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GSBEvaluationModule extends AbstractEvaluationModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(GSBEvaluationModule.class);

    /* Final evaluation model */
    private Model finalModel = ModelFactory.createDefaultModel();

    /* Properties for query execution status */
    private Property EVALUATION_Q01E_STATUS = null;
    private Property EVALUATION_Q02E_STATUS = null;
    private Property EVALUATION_Q03E_STATUS = null;
    private Property EVALUATION_Q04E_STATUS = null;
    private Property EVALUATION_Q05E_STATUS = null;
    private Property EVALUATION_Q06E_STATUS = null;

    /* Property for number of corrent answers" */
    private Property EVALUATION_NUMBER_OF_CORRECT_ANSWERS = null;
	
    private Boolean[] correctAnswers = new Boolean[GSBConstants.GSB_NUMBER_OF_QUERIES];
    private Map<String, ArrayList<Long> > executionStatuses = new HashMap<>();
    
    private long loading_time;
    private long workload_start_time = Long.MAX_VALUE;
    private long workload_end_time = 0;

    @Override
    public void init() throws Exception {
    	LOGGER.info("Initialization begins.");
        // Always init the super class first!
        super.init();
		
        // Your initialization code comes here...
        internalInit();        
        LOGGER.info("Initialization ends.");
    }
    
    private void internalInit() {
        Map<String, String> env = System.getenv();
        
        /* status per query execution */
        if (!env.containsKey(GSBConstants.EVALUATION_Q01E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q01E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q02E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q02E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q03E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q03E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q04E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q04E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q05E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q05E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q06E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q06E_STATUS + "\" from the environment. Aborting.");
        }        
        EVALUATION_Q01E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q01E_STATUS));
        EVALUATION_Q02E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q02E_STATUS));
        EVALUATION_Q03E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q03E_STATUS));
        EVALUATION_Q04E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q04E_STATUS));
        EVALUATION_Q05E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q05E_STATUS));
        EVALUATION_Q06E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q06E_STATUS));

        /* number of correct answers */
        if (!env.containsKey(GSBConstants.EVALUATION_NUMBER_OF_CORRECT_ANSWERS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_NUMBER_OF_CORRECT_ANSWERS + "\" from the environment. Aborting.");
        }
        EVALUATION_NUMBER_OF_CORRECT_ANSWERS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_NUMBER_OF_CORRECT_ANSWERS));        
    }

    @Override
    protected void evaluateResponse(byte[] expectedData, byte[] receivedData, long taskSentTimestamp, long responseReceivedTimestamp) throws Exception {
        String eStr = RabbitMQUtils.readString(expectedData);
        String rStr = RabbitMQUtils.readString(receivedData);
        String [] lines = eStr.split("\n\n");
        String queryIndexString = lines[0].trim().substring(2);
        int queryIndex = Integer.parseInt(queryIndexString) - 1;
        
        eStr = lines[1];
        
        correctAnswers[queryIndex] = (eStr.compareToIgnoreCase(rStr) == 0);
        
        if (!correctAnswers[queryIndex]) {
            LOGGER.info("Wrong answer on query " + queryIndexString);            
            LOGGER.info("Expected: " + eStr);
            LOGGER.info("Actual: " + rStr);
        }

        if (taskSentTimestamp < workload_start_time)
            workload_start_time = taskSentTimestamp;
        if (responseReceivedTimestamp > workload_end_time)
            workload_end_time = responseReceivedTimestamp;
    }

    @Override
    protected Model summarizeEvaluation() throws Exception {
        // All tasks/responsens have been evaluated. Summarize the results,
        // write them into a Jena model and send it to the benchmark controller.
        LOGGER.info("Summarize evaluation...");
        //TODO: remove this sleeping
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (experimentUri == null)
            experimentUri = System.getenv().get(Constants.HOBBIT_EXPERIMENT_URI_KEY);

        Resource experiment = finalModel.createResource(experimentUri);
        finalModel.add(experiment, RDF.type, HOBBIT.Experiment);
        
        LOGGER.info("Loading time - " + loading_time);        

        Literal q01eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[0], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q01E_STATUS, q01eStatusLiteral);
        Literal q02eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[1], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q02E_STATUS, q02eStatusLiteral);
        Literal q03eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[2], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q03E_STATUS, q03eStatusLiteral);
        Literal q04eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[3], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q04E_STATUS, q04eStatusLiteral);
        Literal q05eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[4], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q05E_STATUS, q05eStatusLiteral);
        Literal q06eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[5], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q06E_STATUS, q06eStatusLiteral);
        
        int totalCorrect = 0;
        for (int i=0; i < correctAnswers.length; i++) {
            if (correctAnswers[i])
                totalCorrect += 1;
        }
        
        Literal nbrCrrtAnswrsLiteral = finalModel.createTypedLiteral(totalCorrect, XSDDatatype.XSDinteger);
        finalModel.add(experiment, EVALUATION_NUMBER_OF_CORRECT_ANSWERS, nbrCrrtAnswrsLiteral);

        LOGGER.info(finalModel.toString());
        return finalModel;
    }
	
    @Override
	public void close() throws IOException {
        // Free the resources you requested here
    	LOGGER.info("End of evaluation.");
		
        // Always close the super class after yours!
        super.close();
    }

}
