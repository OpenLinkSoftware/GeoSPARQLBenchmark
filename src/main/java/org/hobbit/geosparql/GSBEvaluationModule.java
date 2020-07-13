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
    private Property EVALUATION_Q04_1E_STATUS = null;
    private Property EVALUATION_Q04_2E_STATUS = null;
    private Property EVALUATION_Q04_3E_STATUS = null;
    private Property EVALUATION_Q04_4E_STATUS = null;
    private Property EVALUATION_Q04_5E_STATUS = null;
    private Property EVALUATION_Q04_6E_STATUS = null;
    private Property EVALUATION_Q04_7E_STATUS = null;
    private Property EVALUATION_Q04_8E_STATUS = null;
    private Property EVALUATION_Q05_1E_STATUS = null;
    private Property EVALUATION_Q05_2E_STATUS = null;
    private Property EVALUATION_Q05_3E_STATUS = null;
    private Property EVALUATION_Q05_4E_STATUS = null;
    private Property EVALUATION_Q05_5E_STATUS = null;
    private Property EVALUATION_Q05_6E_STATUS = null;
    private Property EVALUATION_Q05_7E_STATUS = null;
    private Property EVALUATION_Q05_8E_STATUS = null;
    private Property EVALUATION_Q06_1E_STATUS = null;
    private Property EVALUATION_Q06_2E_STATUS = null;
    private Property EVALUATION_Q06_3E_STATUS = null;
    private Property EVALUATION_Q06_4E_STATUS = null;
    private Property EVALUATION_Q06_5E_STATUS = null;
    private Property EVALUATION_Q06_6E_STATUS = null;
    private Property EVALUATION_Q06_7E_STATUS = null;
    private Property EVALUATION_Q06_8E_STATUS = null;
    private Property EVALUATION_Q07E_STATUS = null;
    private Property EVALUATION_Q08_1E_STATUS = null;
    private Property EVALUATION_Q08_2E_STATUS = null;
    private Property EVALUATION_Q09_1E_STATUS = null;
    private Property EVALUATION_Q09_2E_STATUS = null;
    private Property EVALUATION_Q09_3E_STATUS = null;
    private Property EVALUATION_Q09_4E_STATUS = null;
    private Property EVALUATION_Q09_5E_STATUS = null;
    private Property EVALUATION_Q09_6E_STATUS = null;
    private Property EVALUATION_Q10E_STATUS = null;
    private Property EVALUATION_Q11E_STATUS = null;
    private Property EVALUATION_Q12E_STATUS = null;
    private Property EVALUATION_Q13_1E_STATUS = null;
    private Property EVALUATION_Q13_2E_STATUS = null;
    private Property EVALUATION_Q14E_STATUS = null;
    private Property EVALUATION_Q15E_STATUS = null;
    private Property EVALUATION_Q16_1E_STATUS = null;
    private Property EVALUATION_Q16_2E_STATUS = null;
    // private Property EVALUATION_Q17E_STATUS = null;
    private Property EVALUATION_Q18E_STATUS = null;
    private Property EVALUATION_Q19_1E_STATUS = null;
    private Property EVALUATION_Q19_2E_STATUS = null;
    private Property EVALUATION_Q19_3E_STATUS = null;
    private Property EVALUATION_Q19_4E_STATUS = null;
    private Property EVALUATION_Q19_5E_STATUS = null;
    private Property EVALUATION_Q19_6E_STATUS = null;
    private Property EVALUATION_Q19_7E_STATUS = null;
    private Property EVALUATION_Q19_8E_STATUS = null;
    private Property EVALUATION_Q19_9E_STATUS = null;
    private Property EVALUATION_Q20E_STATUS = null;
    private Property EVALUATION_Q21E_STATUS = null;
    private Property EVALUATION_Q22_1E_STATUS = null;
    private Property EVALUATION_Q22_2E_STATUS = null;
    private Property EVALUATION_Q22_3E_STATUS = null;
    private Property EVALUATION_Q22_4E_STATUS = null;
    private Property EVALUATION_Q22_5E_STATUS = null;
    private Property EVALUATION_Q22_6E_STATUS = null;
    private Property EVALUATION_Q22_7E_STATUS = null;
    private Property EVALUATION_Q22_8E_STATUS = null;
    private Property EVALUATION_Q23_1E_STATUS = null;
    private Property EVALUATION_Q23_2E_STATUS = null;
    private Property EVALUATION_Q23_3E_STATUS = null;
    private Property EVALUATION_Q23_4E_STATUS = null;
    private Property EVALUATION_Q23_5E_STATUS = null;
    private Property EVALUATION_Q23_6E_STATUS = null;
    private Property EVALUATION_Q23_7E_STATUS = null;
    private Property EVALUATION_Q23_8E_STATUS = null;
    private Property EVALUATION_Q24_1E_STATUS = null;
    private Property EVALUATION_Q24_2E_STATUS = null;
    private Property EVALUATION_Q24_3E_STATUS = null;
    private Property EVALUATION_Q24_4E_STATUS = null;
    private Property EVALUATION_Q24_5E_STATUS = null;
    private Property EVALUATION_Q24_6E_STATUS = null;
    private Property EVALUATION_Q24_7E_STATUS = null;
    private Property EVALUATION_Q24_8E_STATUS = null;
    private Property EVALUATION_Q25_1E_STATUS = null;
    private Property EVALUATION_Q25_2E_STATUS = null;
    private Property EVALUATION_Q25_3E_STATUS = null;
    private Property EVALUATION_Q26_1E_STATUS = null;
    private Property EVALUATION_Q26_2E_STATUS = null;
    private Property EVALUATION_Q27E_STATUS = null;
    private Property EVALUATION_Q28_1E_STATUS = null;
    private Property EVALUATION_Q28_2E_STATUS = null;
    private Property EVALUATION_Q28_3E_STATUS = null;
    private Property EVALUATION_Q28_4E_STATUS = null;
    private Property EVALUATION_Q28_5E_STATUS = null;
    private Property EVALUATION_Q28_6E_STATUS = null;
    private Property EVALUATION_Q28_7E_STATUS = null;
    private Property EVALUATION_Q28_8E_STATUS = null;
    private Property EVALUATION_Q29_1E_STATUS = null;
    private Property EVALUATION_Q29_2E_STATUS = null;
    private Property EVALUATION_Q29_3E_STATUS = null;
    private Property EVALUATION_Q29_4E_STATUS = null;
    private Property EVALUATION_Q29_5E_STATUS = null;
    private Property EVALUATION_Q29_6E_STATUS = null;
    private Property EVALUATION_Q29_7E_STATUS = null;
    private Property EVALUATION_Q29_8E_STATUS = null;
    private Property EVALUATION_Q30_1E_STATUS = null;
    private Property EVALUATION_Q30_2E_STATUS = null;
    private Property EVALUATION_Q30_3E_STATUS = null;
    private Property EVALUATION_Q30_4E_STATUS = null;
    private Property EVALUATION_Q30_5E_STATUS = null;
    private Property EVALUATION_Q30_6E_STATUS = null;
    private Property EVALUATION_Q30_7E_STATUS = null;
    private Property EVALUATION_Q30_8E_STATUS = null;

    /* Property for number of correct answers" */
    private Property EVALUATION_NUMBER_OF_CORRECT_ANSWERS = null;

    /* Property for number of correct requirements" */
    private Property EVALUATION_PERCENTAGE_OF_SATISFIED_REQUIREMENTS = null;
	
    private Boolean[] correctAnswers = new Boolean[GSBConstants.GSB_NUMBER_OF_QUERIES];
    private Boolean[] satisfiedRequirements = new Boolean[GSBConstants.GSB_NUMBER_OF_REQUIREMENTS];
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
        if (!env.containsKey(GSBConstants.EVALUATION_Q04_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q04_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q04_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q04_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q04_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q04_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q04_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q04_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q04_5E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q04_5E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q04_6E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q04_6E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q04_7E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q04_7E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q04_8E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q04_8E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q05_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q05_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q05_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q05_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q05_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q05_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q05_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q05_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q05_5E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q05_5E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q05_6E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q05_6E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q05_7E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q05_7E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q05_8E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q05_8E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q06_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q06_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q06_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q06_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q06_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q06_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q06_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q06_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q06_5E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q06_5E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q06_6E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q06_6E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q06_7E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q06_7E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q06_8E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q06_8E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q07E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q07E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q08_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q08_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q08_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q08_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q09_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q09_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q09_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q09_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q09_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q09_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q09_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q09_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q09_5E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q09_5E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q09_6E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q09_6E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q10E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q10E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q11E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q11E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q12E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q12E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q13_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q13_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q13_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q13_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q14E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q14E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q15E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q15E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q16_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q16_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q16_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q16_2E_STATUS + "\" from the environment. Aborting.");
        }
//        if (!env.containsKey(GSBConstants.EVALUATION_Q17E_STATUS)) {
//            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q17E_STATUS + "\" from the environment. Aborting.");
//        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q18E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q18E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_5E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_5E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_6E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_6E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_7E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_7E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_8E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_8E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_9E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_9E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q20E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q20E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q21E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q21E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_5E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_5E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_6E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_6E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_7E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_7E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_8E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_8E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_5E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_5E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_6E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_6E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_7E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_7E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_8E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_8E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_5E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_5E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_6E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_6E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_7E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_7E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_8E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_8E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q25_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q25_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q25_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q25_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q25_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q25_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q26_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q26_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q26_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q26_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q27E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q27E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q28_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q28_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q28_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q28_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q28_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q28_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q28_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q28_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q28_5E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q28_5E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q28_6E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q28_6E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q28_7E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q28_7E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q28_8E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q28_8E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q29_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q29_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q29_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q29_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q29_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q29_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q29_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q29_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q29_5E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q29_5E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q29_6E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q29_6E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q29_7E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q29_7E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q29_8E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q29_8E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q30_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q30_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q30_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q30_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q30_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q30_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q30_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q30_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q30_5E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q30_5E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q30_6E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q30_6E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q30_7E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q30_7E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q30_8E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q30_8E_STATUS + "\" from the environment. Aborting.");
        }
        EVALUATION_Q01E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q01E_STATUS));
        EVALUATION_Q02E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q02E_STATUS));
        EVALUATION_Q03E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q03E_STATUS));
        EVALUATION_Q04_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q04_1E_STATUS));
        EVALUATION_Q04_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q04_2E_STATUS));
        EVALUATION_Q04_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q04_3E_STATUS));
        EVALUATION_Q04_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q04_4E_STATUS));
        EVALUATION_Q04_5E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q04_5E_STATUS));
        EVALUATION_Q04_6E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q04_6E_STATUS));
        EVALUATION_Q04_7E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q04_7E_STATUS));
        EVALUATION_Q04_8E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q04_8E_STATUS));
        EVALUATION_Q05_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q05_1E_STATUS));
        EVALUATION_Q05_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q05_2E_STATUS));
        EVALUATION_Q05_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q05_3E_STATUS));
        EVALUATION_Q05_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q05_4E_STATUS));
        EVALUATION_Q05_5E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q05_5E_STATUS));
        EVALUATION_Q05_6E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q05_6E_STATUS));
        EVALUATION_Q05_7E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q05_7E_STATUS));
        EVALUATION_Q05_8E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q05_8E_STATUS));
        EVALUATION_Q06_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q06_1E_STATUS));
        EVALUATION_Q06_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q06_2E_STATUS));
        EVALUATION_Q06_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q06_3E_STATUS));
        EVALUATION_Q06_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q06_4E_STATUS));
        EVALUATION_Q06_5E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q06_5E_STATUS));
        EVALUATION_Q06_6E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q06_6E_STATUS));
        EVALUATION_Q06_7E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q06_7E_STATUS));
        EVALUATION_Q06_8E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q06_8E_STATUS));
        EVALUATION_Q07E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q07E_STATUS));
        EVALUATION_Q08_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q08_1E_STATUS));
        EVALUATION_Q08_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q08_2E_STATUS));
        EVALUATION_Q09_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q09_1E_STATUS));
        EVALUATION_Q09_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q09_2E_STATUS));
        EVALUATION_Q09_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q09_3E_STATUS));
        EVALUATION_Q09_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q09_4E_STATUS));
        EVALUATION_Q09_5E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q09_5E_STATUS));
        EVALUATION_Q09_6E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q09_6E_STATUS));
        EVALUATION_Q10E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q10E_STATUS));
        EVALUATION_Q11E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q11E_STATUS));
        EVALUATION_Q12E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q12E_STATUS));
        EVALUATION_Q13_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q13_1E_STATUS));
        EVALUATION_Q13_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q13_2E_STATUS));
        EVALUATION_Q14E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q14E_STATUS));
        EVALUATION_Q15E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q15E_STATUS));
        EVALUATION_Q16_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q16_1E_STATUS));
        EVALUATION_Q16_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q16_2E_STATUS));
        // EVALUATION_Q17E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q17E_STATUS));
        EVALUATION_Q18E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q18E_STATUS));
        EVALUATION_Q19_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_1E_STATUS));
        EVALUATION_Q19_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_2E_STATUS));
        EVALUATION_Q19_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_3E_STATUS));
        EVALUATION_Q19_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_4E_STATUS));
        EVALUATION_Q19_5E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_5E_STATUS));
        EVALUATION_Q19_6E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_6E_STATUS));
        EVALUATION_Q19_7E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_7E_STATUS));
        EVALUATION_Q19_8E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_8E_STATUS));
        EVALUATION_Q19_9E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_9E_STATUS));
        EVALUATION_Q20E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q20E_STATUS));
        EVALUATION_Q21E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q21E_STATUS));
        EVALUATION_Q22_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_1E_STATUS));
        EVALUATION_Q22_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_2E_STATUS));
        EVALUATION_Q22_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_3E_STATUS));
        EVALUATION_Q22_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_4E_STATUS));
        EVALUATION_Q22_5E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_5E_STATUS));
        EVALUATION_Q22_6E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_6E_STATUS));
        EVALUATION_Q22_7E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_7E_STATUS));
        EVALUATION_Q22_8E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_8E_STATUS));
        EVALUATION_Q23_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_1E_STATUS));
        EVALUATION_Q23_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_2E_STATUS));
        EVALUATION_Q23_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_3E_STATUS));
        EVALUATION_Q23_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_4E_STATUS));
        EVALUATION_Q23_5E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_5E_STATUS));
        EVALUATION_Q23_6E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_6E_STATUS));
        EVALUATION_Q23_7E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_7E_STATUS));
        EVALUATION_Q23_8E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_8E_STATUS));
        EVALUATION_Q24_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_1E_STATUS));
        EVALUATION_Q24_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_2E_STATUS));
        EVALUATION_Q24_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_3E_STATUS));
        EVALUATION_Q24_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_4E_STATUS));
        EVALUATION_Q24_5E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_5E_STATUS));
        EVALUATION_Q24_6E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_6E_STATUS));
        EVALUATION_Q24_7E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_7E_STATUS));
        EVALUATION_Q24_8E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_8E_STATUS));
        EVALUATION_Q25_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q25_1E_STATUS));
        EVALUATION_Q25_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q25_2E_STATUS));
        EVALUATION_Q25_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q25_3E_STATUS));
        EVALUATION_Q26_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q26_1E_STATUS));
        EVALUATION_Q26_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q26_2E_STATUS));
        EVALUATION_Q27E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q27E_STATUS));
        EVALUATION_Q28_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q28_1E_STATUS));
        EVALUATION_Q28_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q28_2E_STATUS));
        EVALUATION_Q28_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q28_3E_STATUS));
        EVALUATION_Q28_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q28_4E_STATUS));
        EVALUATION_Q28_5E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q28_5E_STATUS));
        EVALUATION_Q28_6E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q28_6E_STATUS));
        EVALUATION_Q28_7E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q28_7E_STATUS));
        EVALUATION_Q28_8E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q28_8E_STATUS));
        EVALUATION_Q29_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q29_1E_STATUS));
        EVALUATION_Q29_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q29_2E_STATUS));
        EVALUATION_Q29_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q29_3E_STATUS));
        EVALUATION_Q29_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q29_4E_STATUS));
        EVALUATION_Q29_5E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q29_5E_STATUS));
        EVALUATION_Q29_6E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q29_6E_STATUS));
        EVALUATION_Q29_7E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q29_7E_STATUS));
        EVALUATION_Q29_8E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q29_8E_STATUS));
        EVALUATION_Q30_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q30_1E_STATUS));
        EVALUATION_Q30_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q30_2E_STATUS));
        EVALUATION_Q30_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q30_3E_STATUS));
        EVALUATION_Q30_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q30_4E_STATUS));
        EVALUATION_Q30_5E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q30_5E_STATUS));
        EVALUATION_Q30_6E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q30_6E_STATUS));
        EVALUATION_Q30_7E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q30_7E_STATUS));
        EVALUATION_Q30_8E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q30_8E_STATUS));

        /* number of correct answers */
        if (!env.containsKey(GSBConstants.EVALUATION_NUMBER_OF_CORRECT_ANSWERS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_NUMBER_OF_CORRECT_ANSWERS + "\" from the environment. Aborting.");
        }
        EVALUATION_NUMBER_OF_CORRECT_ANSWERS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_NUMBER_OF_CORRECT_ANSWERS));

        /* number of correct requirements */
        if (!env.containsKey(GSBConstants.EVALUATION_PERCENTAGE_OF_SATISFIED_REQUIREMENTS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_PERCENTAGE_OF_SATISFIED_REQUIREMENTS + "\" from the environment. Aborting.");
        }
        EVALUATION_PERCENTAGE_OF_SATISFIED_REQUIREMENTS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_PERCENTAGE_OF_SATISFIED_REQUIREMENTS));
    }

    @Override
    protected void evaluateResponse(byte[] expectedData, byte[] receivedData, long taskSentTimestamp, long responseReceivedTimestamp) throws Exception {
        String eStr = RabbitMQUtils.readString(expectedData);
        String rStr = RabbitMQUtils.readString(receivedData);
        String [] lines = eStr.split("\n\n");
        String queryIndexString = lines[0].trim().substring(3);
        int queryIndex = Integer.parseInt(queryIndexString) - 1;
        
        eStr = lines[1];
        
        correctAnswers[queryIndex] = (eStr.compareToIgnoreCase(rStr) == 0);
        
        if (!correctAnswers[queryIndex]) {
            LOGGER.info("Wrong answer on query " + queryIndexString);            
            LOGGER.info("Expected: " + eStr);
            LOGGER.info("Actual: " + rStr);
        }

        if (correctAnswers[queryIndex]) {
            LOGGER.info("Correct answer on query " + queryIndexString);
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
        // All tasks/responses have been evaluated. Summarize the results,
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
        Literal q04_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[3], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q04_1E_STATUS, q04_1eStatusLiteral);
        Literal q04_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[4], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q04_2E_STATUS, q04_2eStatusLiteral);
        Literal q04_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[5], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q04_3E_STATUS, q04_3eStatusLiteral);
        Literal q04_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[6], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q04_4E_STATUS, q04_4eStatusLiteral);
        Literal q04_5eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[7], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q04_5E_STATUS, q04_5eStatusLiteral);
        Literal q04_6eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[8], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q04_6E_STATUS, q04_6eStatusLiteral);
        Literal q04_7eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[9], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q04_7E_STATUS, q04_7eStatusLiteral);
        Literal q04_8eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[10], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q04_8E_STATUS, q04_8eStatusLiteral);
        Literal q05_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[11], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q05_1E_STATUS, q05_1eStatusLiteral);
        Literal q05_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[12], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q05_2E_STATUS, q05_2eStatusLiteral);
        Literal q05_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[13], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q05_3E_STATUS, q05_3eStatusLiteral);
        Literal q05_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[14], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q05_4E_STATUS, q05_4eStatusLiteral);
        Literal q05_5eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[15], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q05_5E_STATUS, q05_5eStatusLiteral);
        Literal q05_6eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[16], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q05_6E_STATUS, q05_6eStatusLiteral);
        Literal q05_7eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[17], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q05_7E_STATUS, q05_7eStatusLiteral);
        Literal q05_8eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[18], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q05_8E_STATUS, q05_8eStatusLiteral);
        Literal q06_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[19], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q06_1E_STATUS, q06_1eStatusLiteral);
        Literal q06_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[20], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q06_2E_STATUS, q06_2eStatusLiteral);
        Literal q06_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[21], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q06_3E_STATUS, q06_3eStatusLiteral);
        Literal q06_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[22], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q06_4E_STATUS, q06_4eStatusLiteral);
        Literal q06_5eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[23], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q06_5E_STATUS, q06_5eStatusLiteral);
        Literal q06_6eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[24], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q06_6E_STATUS, q06_6eStatusLiteral);
        Literal q06_7eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[25], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q06_7E_STATUS, q06_7eStatusLiteral);
        Literal q06_8eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[26], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q06_8E_STATUS, q06_8eStatusLiteral);
        Literal q07eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[27], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q07E_STATUS, q07eStatusLiteral);
        Literal q08_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[28], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q08_1E_STATUS, q08_1eStatusLiteral);
        Literal q08_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[29], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q08_2E_STATUS, q08_2eStatusLiteral);
        Literal q09_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[30], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q09_1E_STATUS, q09_1eStatusLiteral);
        Literal q09_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[31], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q09_2E_STATUS, q09_2eStatusLiteral);
        Literal q09_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[32], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q09_3E_STATUS, q09_3eStatusLiteral);
        Literal q09_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[33], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q09_4E_STATUS, q09_4eStatusLiteral);
        Literal q09_5eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[34], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q09_5E_STATUS, q09_5eStatusLiteral);
        Literal q09_6eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[35], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q09_6E_STATUS, q09_6eStatusLiteral);
        Literal q10eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[36], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q10E_STATUS, q10eStatusLiteral);
        Literal q11eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[37], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q11E_STATUS, q11eStatusLiteral);
        Literal q12eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[38], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q12E_STATUS, q12eStatusLiteral);
        Literal q13_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[39], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q13_1E_STATUS, q13_1eStatusLiteral);
        Literal q13_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[40], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q13_2E_STATUS, q13_2eStatusLiteral);
        Literal q14eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[41], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q14E_STATUS, q14eStatusLiteral);
        Literal q15eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[42], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q15E_STATUS, q15eStatusLiteral);
        Literal q16_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[43], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q16_1E_STATUS, q16_1eStatusLiteral);
        Literal q16_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[44], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q16_2E_STATUS, q16_2eStatusLiteral);
        Literal q18eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[45], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q18E_STATUS, q18eStatusLiteral);
        Literal q19_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[46], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_1E_STATUS, q19_1eStatusLiteral);
        Literal q19_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[47], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_2E_STATUS, q19_2eStatusLiteral);
        Literal q19_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[48], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_3E_STATUS, q19_3eStatusLiteral);
        Literal q19_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[49], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_4E_STATUS, q19_4eStatusLiteral);
        Literal q19_5eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[50], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_5E_STATUS, q19_5eStatusLiteral);
        Literal q19_6eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[51], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_6E_STATUS, q19_6eStatusLiteral);
        Literal q19_7eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[52], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_7E_STATUS, q19_7eStatusLiteral);
        Literal q19_8eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[53], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_8E_STATUS, q19_8eStatusLiteral);
        Literal q19_9eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[54], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_9E_STATUS, q19_9eStatusLiteral);
        Literal q20eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[55], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q20E_STATUS, q20eStatusLiteral);
        Literal q21eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[56], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q21E_STATUS, q21eStatusLiteral);
        Literal q22_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[57], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_1E_STATUS, q22_1eStatusLiteral);
        Literal q22_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[58], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_2E_STATUS, q22_2eStatusLiteral);
        Literal q22_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[59], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_3E_STATUS, q22_3eStatusLiteral);
        Literal q22_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[60], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_4E_STATUS, q22_4eStatusLiteral);
        Literal q22_5eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[61], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_5E_STATUS, q22_5eStatusLiteral);
        Literal q22_6eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[62], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_6E_STATUS, q22_6eStatusLiteral);
        Literal q22_7eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[63], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_7E_STATUS, q22_7eStatusLiteral);
        Literal q22_8eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[64], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_8E_STATUS, q22_8eStatusLiteral);
        Literal q23_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[65], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_1E_STATUS, q23_1eStatusLiteral);
        Literal q23_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[66], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_2E_STATUS, q23_2eStatusLiteral);
        Literal q23_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[67], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_3E_STATUS, q23_3eStatusLiteral);
        Literal q23_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[68], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_4E_STATUS, q23_4eStatusLiteral);
        Literal q23_5eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[69], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_5E_STATUS, q23_5eStatusLiteral);
        Literal q23_6eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[70], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_6E_STATUS, q23_6eStatusLiteral);
        Literal q23_7eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[71], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_7E_STATUS, q23_7eStatusLiteral);
        Literal q23_8eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[72], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_8E_STATUS, q23_8eStatusLiteral);
        Literal q24_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[73], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_1E_STATUS, q24_1eStatusLiteral);
        Literal q24_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[74], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_2E_STATUS, q24_2eStatusLiteral);
        Literal q24_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[75], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_3E_STATUS, q24_3eStatusLiteral);
        Literal q24_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[76], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_4E_STATUS, q24_4eStatusLiteral);
        Literal q24_5eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[77], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_5E_STATUS, q24_5eStatusLiteral);
        Literal q24_6eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[78], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_6E_STATUS, q24_6eStatusLiteral);
        Literal q24_7eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[79], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_7E_STATUS, q24_7eStatusLiteral);
        Literal q24_8eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[80], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_8E_STATUS, q24_8eStatusLiteral);
        Literal q25_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[81], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q25_1E_STATUS, q25_1eStatusLiteral);
        Literal q25_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[82], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q25_2E_STATUS, q25_2eStatusLiteral);
        Literal q25_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[83], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q25_3E_STATUS, q25_3eStatusLiteral);
        Literal q26_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[84], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q26_1E_STATUS, q26_1eStatusLiteral);
        Literal q26_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[85], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q26_2E_STATUS, q26_2eStatusLiteral);
        Literal q27eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[86], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q27E_STATUS, q27eStatusLiteral);
        Literal q28_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[87], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q28_1E_STATUS, q28_1eStatusLiteral);
        Literal q28_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[88], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q28_2E_STATUS, q28_2eStatusLiteral);
        Literal q28_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[89], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q28_3E_STATUS, q28_3eStatusLiteral);
        Literal q28_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[90], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q28_4E_STATUS, q28_4eStatusLiteral);
        Literal q28_5eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[91], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q28_5E_STATUS, q28_5eStatusLiteral);
        Literal q28_6eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[92], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q28_6E_STATUS, q28_6eStatusLiteral);
        Literal q28_7eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[93], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q28_7E_STATUS, q28_7eStatusLiteral);
        Literal q28_8eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[94], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q28_8E_STATUS, q28_8eStatusLiteral);
        Literal q29_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[95], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q29_1E_STATUS, q29_1eStatusLiteral);
        Literal q29_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[96], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q29_2E_STATUS, q29_2eStatusLiteral);
        Literal q29_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[97], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q29_3E_STATUS, q29_3eStatusLiteral);
        Literal q29_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[98], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q29_4E_STATUS, q29_4eStatusLiteral);
        Literal q29_5eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[99], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q29_5E_STATUS, q29_5eStatusLiteral);
        Literal q29_6eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[100], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q29_6E_STATUS, q29_6eStatusLiteral);
        Literal q29_7eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[101], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q29_7E_STATUS, q29_7eStatusLiteral);
        Literal q29_8eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[102], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q29_8E_STATUS, q29_8eStatusLiteral);
        Literal q30_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[103], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q30_1E_STATUS, q30_1eStatusLiteral);
        Literal q30_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[104], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q30_2E_STATUS, q30_2eStatusLiteral);
        Literal q30_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[105], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q30_3E_STATUS, q30_3eStatusLiteral);
        Literal q30_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[106], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q30_4E_STATUS, q30_4eStatusLiteral);
        Literal q30_5eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[107], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q30_5E_STATUS, q30_5eStatusLiteral);
        Literal q30_6eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[108], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q30_6E_STATUS, q30_6eStatusLiteral);
        Literal q30_7eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[109], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q30_7E_STATUS, q30_7eStatusLiteral);
        Literal q30_8eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[110], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q30_8E_STATUS, q30_8eStatusLiteral);
        
        int totalCorrect = 0;
        double percentageCorrect = 0.0;
        for (int i=0; i < correctAnswers.length; i++) {
            if (correctAnswers[i])
                totalCorrect += 1;
                percentageCorrect += GSBConstants.GSB_ANSWERS_WEIGHTS[i];
        }

        percentageCorrect += 1.0 / 30.0; // Adding one 'correct' answer, for Req. 17 which is not tested
        percentageCorrect = percentageCorrect / 30.0 * 100.0; // Transforming the result into a 0-100% range
        
        Literal numberOfCorrectAnswersLiteral = finalModel.createTypedLiteral(totalCorrect, XSDDatatype.XSDinteger);
        finalModel.add(experiment, EVALUATION_NUMBER_OF_CORRECT_ANSWERS, numberOfCorrectAnswersLiteral);

        Literal percentageOfCorrectAnswersLiteral = finalModel.createTypedLiteral(percentageCorrect, XSDDatatype.XSDfloat);
        finalModel.add(experiment, EVALUATION_PERCENTAGE_OF_SATISFIED_REQUIREMENTS, percentageOfCorrectAnswersLiteral);

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
