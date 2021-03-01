package org.hobbit.geosparql;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.apache.xml.security.parser.XMLParserException;
import org.hobbit.core.Constants;
import org.hobbit.core.components.AbstractEvaluationModule;
import org.hobbit.core.rabbit.RabbitMQUtils;
import org.hobbit.geosparql.util.GSBConstants;
import org.hobbit.vocab.HOBBIT;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    private Property EVALUATION_Q19_1_1E_STATUS = null;
    private Property EVALUATION_Q19_1_2E_STATUS = null;
    private Property EVALUATION_Q19_1_3E_STATUS = null;
    private Property EVALUATION_Q19_1_4E_STATUS = null;
    private Property EVALUATION_Q19_2_1E_STATUS = null;
    private Property EVALUATION_Q19_2_2E_STATUS = null;
    private Property EVALUATION_Q19_3_1E_STATUS = null;
    private Property EVALUATION_Q19_3_2E_STATUS = null;
    private Property EVALUATION_Q19_4_1E_STATUS = null;
    private Property EVALUATION_Q19_4_2E_STATUS = null;
    private Property EVALUATION_Q19_4_3E_STATUS = null;
    private Property EVALUATION_Q19_4_4E_STATUS = null;
    private Property EVALUATION_Q19_5_1E_STATUS = null;
    private Property EVALUATION_Q19_5_2E_STATUS = null;
    private Property EVALUATION_Q19_5_3E_STATUS = null;
    private Property EVALUATION_Q19_5_4E_STATUS = null;
    private Property EVALUATION_Q19_6_1E_STATUS = null;
    private Property EVALUATION_Q19_6_2E_STATUS = null;
    private Property EVALUATION_Q19_6_3E_STATUS = null;
    private Property EVALUATION_Q19_6_4E_STATUS = null;
    private Property EVALUATION_Q19_7_1E_STATUS = null;
    private Property EVALUATION_Q19_7_2E_STATUS = null;
    private Property EVALUATION_Q19_7_3E_STATUS = null;
    private Property EVALUATION_Q19_7_4E_STATUS = null;
    private Property EVALUATION_Q19_8_1E_STATUS = null;
    private Property EVALUATION_Q19_8_2E_STATUS = null;
    private Property EVALUATION_Q19_9_1E_STATUS = null;
    private Property EVALUATION_Q19_9_2E_STATUS = null;
    private Property EVALUATION_Q20_1E_STATUS = null;
    private Property EVALUATION_Q20_2E_STATUS = null;
    private Property EVALUATION_Q21_1E_STATUS = null;
    private Property EVALUATION_Q21_2E_STATUS = null;
    private Property EVALUATION_Q21_3E_STATUS = null;
    private Property EVALUATION_Q21_4E_STATUS = null;
    private Property EVALUATION_Q22_1_1E_STATUS = null;
    private Property EVALUATION_Q22_1_2E_STATUS = null;
    private Property EVALUATION_Q22_1_3E_STATUS = null;
    private Property EVALUATION_Q22_1_4E_STATUS = null;
    private Property EVALUATION_Q22_2_1E_STATUS = null;
    private Property EVALUATION_Q22_2_2E_STATUS = null;
    private Property EVALUATION_Q22_2_3E_STATUS = null;
    private Property EVALUATION_Q22_2_4E_STATUS = null;
    private Property EVALUATION_Q22_3_1E_STATUS = null;
    private Property EVALUATION_Q22_3_2E_STATUS = null;
    private Property EVALUATION_Q22_3_3E_STATUS = null;
    private Property EVALUATION_Q22_3_4E_STATUS = null;
    private Property EVALUATION_Q22_4_1E_STATUS = null;
    private Property EVALUATION_Q22_4_2E_STATUS = null;
    private Property EVALUATION_Q22_4_3E_STATUS = null;
    private Property EVALUATION_Q22_4_4E_STATUS = null;
    private Property EVALUATION_Q22_5_1E_STATUS = null;
    private Property EVALUATION_Q22_5_2E_STATUS = null;
    private Property EVALUATION_Q22_5_3E_STATUS = null;
    private Property EVALUATION_Q22_5_4E_STATUS = null;
    private Property EVALUATION_Q22_6_1E_STATUS = null;
    private Property EVALUATION_Q22_6_2E_STATUS = null;
    private Property EVALUATION_Q22_6_3E_STATUS = null;
    private Property EVALUATION_Q22_6_4E_STATUS = null;
    private Property EVALUATION_Q22_7_1E_STATUS = null;
    private Property EVALUATION_Q22_7_2E_STATUS = null;
    private Property EVALUATION_Q22_7_3E_STATUS = null;
    private Property EVALUATION_Q22_7_4E_STATUS = null;
    private Property EVALUATION_Q22_8_1E_STATUS = null;
    private Property EVALUATION_Q22_8_2E_STATUS = null;
    private Property EVALUATION_Q22_8_3E_STATUS = null;
    private Property EVALUATION_Q22_8_4E_STATUS = null;
    private Property EVALUATION_Q23_1_1E_STATUS = null;
    private Property EVALUATION_Q23_1_2E_STATUS = null;
    private Property EVALUATION_Q23_1_3E_STATUS = null;
    private Property EVALUATION_Q23_1_4E_STATUS = null;
    private Property EVALUATION_Q23_2_1E_STATUS = null;
    private Property EVALUATION_Q23_2_2E_STATUS = null;
    private Property EVALUATION_Q23_2_3E_STATUS = null;
    private Property EVALUATION_Q23_2_4E_STATUS = null;
    private Property EVALUATION_Q23_3_1E_STATUS = null;
    private Property EVALUATION_Q23_3_2E_STATUS = null;
    private Property EVALUATION_Q23_3_3E_STATUS = null;
    private Property EVALUATION_Q23_3_4E_STATUS = null;
    private Property EVALUATION_Q23_4_1E_STATUS = null;
    private Property EVALUATION_Q23_4_2E_STATUS = null;
    private Property EVALUATION_Q23_4_3E_STATUS = null;
    private Property EVALUATION_Q23_4_4E_STATUS = null;
    private Property EVALUATION_Q23_5_1E_STATUS = null;
    private Property EVALUATION_Q23_5_2E_STATUS = null;
    private Property EVALUATION_Q23_5_3E_STATUS = null;
    private Property EVALUATION_Q23_5_4E_STATUS = null;
    private Property EVALUATION_Q23_6_1E_STATUS = null;
    private Property EVALUATION_Q23_6_2E_STATUS = null;
    private Property EVALUATION_Q23_6_3E_STATUS = null;
    private Property EVALUATION_Q23_6_4E_STATUS = null;
    private Property EVALUATION_Q23_7_1E_STATUS = null;
    private Property EVALUATION_Q23_7_2E_STATUS = null;
    private Property EVALUATION_Q23_7_3E_STATUS = null;
    private Property EVALUATION_Q23_7_4E_STATUS = null;
    private Property EVALUATION_Q23_8_1E_STATUS = null;
    private Property EVALUATION_Q23_8_2E_STATUS = null;
    private Property EVALUATION_Q23_8_3E_STATUS = null;
    private Property EVALUATION_Q23_8_4E_STATUS = null;
    private Property EVALUATION_Q24_1_1E_STATUS = null;
    private Property EVALUATION_Q24_1_2E_STATUS = null;
    private Property EVALUATION_Q24_1_3E_STATUS = null;
    private Property EVALUATION_Q24_1_4E_STATUS = null;
    private Property EVALUATION_Q24_2_1E_STATUS = null;
    private Property EVALUATION_Q24_2_2E_STATUS = null;
    private Property EVALUATION_Q24_2_3E_STATUS = null;
    private Property EVALUATION_Q24_2_4E_STATUS = null;
    private Property EVALUATION_Q24_3_1E_STATUS = null;
    private Property EVALUATION_Q24_3_2E_STATUS = null;
    private Property EVALUATION_Q24_3_3E_STATUS = null;
    private Property EVALUATION_Q24_3_4E_STATUS = null;
    private Property EVALUATION_Q24_4_1E_STATUS = null;
    private Property EVALUATION_Q24_4_2E_STATUS = null;
    private Property EVALUATION_Q24_4_3E_STATUS = null;
    private Property EVALUATION_Q24_4_4E_STATUS = null;
    private Property EVALUATION_Q24_5_1E_STATUS = null;
    private Property EVALUATION_Q24_5_2E_STATUS = null;
    private Property EVALUATION_Q24_5_3E_STATUS = null;
    private Property EVALUATION_Q24_5_4E_STATUS = null;
    private Property EVALUATION_Q24_6_1E_STATUS = null;
    private Property EVALUATION_Q24_6_2E_STATUS = null;
    private Property EVALUATION_Q24_6_3E_STATUS = null;
    private Property EVALUATION_Q24_6_4E_STATUS = null;
    private Property EVALUATION_Q24_7_1E_STATUS = null;
    private Property EVALUATION_Q24_7_2E_STATUS = null;
    private Property EVALUATION_Q24_7_3E_STATUS = null;
    private Property EVALUATION_Q24_7_4E_STATUS = null;
    private Property EVALUATION_Q24_8_1E_STATUS = null;
    private Property EVALUATION_Q24_8_2E_STATUS = null;
    private Property EVALUATION_Q24_8_3E_STATUS = null;
    private Property EVALUATION_Q24_8_4E_STATUS = null;
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
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_1_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_1_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_1_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_1_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_1_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_1_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_1_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_1_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_2_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_2_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_2_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_2_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_3_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_3_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_3_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_3_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_4_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_4_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_4_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_4_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_4_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_4_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_4_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_4_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_5_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_5_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_5_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_5_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_5_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_5_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_5_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_5_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_6_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_6_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_6_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_6_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_6_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_6_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_6_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_6_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_7_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_7_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_7_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_7_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_7_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_7_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_7_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_7_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_8_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_8_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_8_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_8_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_9_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_9_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q19_9_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q19_9_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q20_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q20_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q20_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q20_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q21_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q21_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q21_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q21_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q21_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q21_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q21_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q21_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_1_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_1_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_1_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_1_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_1_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_1_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_1_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_1_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_2_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_2_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_2_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_2_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_2_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_2_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_2_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_2_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_3_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_3_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_3_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_3_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_3_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_3_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_3_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_3_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_4_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_4_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_4_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_4_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_4_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_4_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_4_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_4_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_5_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_5_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_5_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_5_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_5_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_5_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_5_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_5_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_6_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_6_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_6_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_6_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_6_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_6_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_6_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_6_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_7_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_7_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_7_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_7_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_7_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_7_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_7_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_7_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_8_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_8_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_8_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_8_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_8_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_8_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q22_8_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q22_8_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_1_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_1_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_1_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_1_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_1_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_1_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_1_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_1_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_2_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_2_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_2_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_2_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_2_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_2_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_2_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_2_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_3_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_3_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_3_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_3_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_3_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_3_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_3_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_3_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_4_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_4_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_4_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_4_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_4_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_4_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_4_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_4_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_5_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_5_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_5_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_5_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_5_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_5_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_5_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_5_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_6_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_6_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_6_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_6_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_6_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_6_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_6_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_6_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_7_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_7_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_7_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_7_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_7_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_7_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_7_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_7_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_8_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_8_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_8_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_8_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_8_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_8_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q23_8_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q23_8_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_1_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_1_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_1_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_1_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_1_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_1_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_1_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_1_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_2_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_2_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_2_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_2_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_2_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_2_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_2_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_2_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_3_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_3_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_3_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_3_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_3_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_3_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_3_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_3_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_4_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_4_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_4_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_4_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_4_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_4_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_4_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_4_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_5_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_5_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_5_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_5_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_5_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_5_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_5_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_5_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_6_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_6_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_6_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_6_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_6_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_6_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_6_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_6_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_7_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_7_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_7_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_7_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_7_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_7_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_7_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_7_4E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_8_1E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_8_1E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_8_2E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_8_2E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_8_3E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_8_3E_STATUS + "\" from the environment. Aborting.");
        }
        if (!env.containsKey(GSBConstants.EVALUATION_Q24_8_4E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get \"" + GSBConstants.EVALUATION_Q24_8_4E_STATUS + "\" from the environment. Aborting.");
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
        EVALUATION_Q19_1_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_1_1E_STATUS));
        EVALUATION_Q19_1_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_1_2E_STATUS));
        EVALUATION_Q19_1_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_1_3E_STATUS));
        EVALUATION_Q19_1_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_1_4E_STATUS));
        EVALUATION_Q19_2_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_2_1E_STATUS));
        EVALUATION_Q19_2_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_2_2E_STATUS));
        EVALUATION_Q19_3_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_3_1E_STATUS));
        EVALUATION_Q19_3_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_3_2E_STATUS));
        EVALUATION_Q19_4_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_4_1E_STATUS));
        EVALUATION_Q19_4_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_4_2E_STATUS));
        EVALUATION_Q19_4_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_4_3E_STATUS));
        EVALUATION_Q19_4_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_4_4E_STATUS));
        EVALUATION_Q19_5_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_5_1E_STATUS));
        EVALUATION_Q19_5_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_5_2E_STATUS));
        EVALUATION_Q19_5_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_5_3E_STATUS));
        EVALUATION_Q19_5_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_5_4E_STATUS));
        EVALUATION_Q19_6_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_6_1E_STATUS));
        EVALUATION_Q19_6_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_6_2E_STATUS));
        EVALUATION_Q19_6_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_6_3E_STATUS));
        EVALUATION_Q19_6_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_6_4E_STATUS));
        EVALUATION_Q19_7_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_7_1E_STATUS));
        EVALUATION_Q19_7_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_7_2E_STATUS));
        EVALUATION_Q19_7_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_7_3E_STATUS));
        EVALUATION_Q19_7_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_7_4E_STATUS));
        EVALUATION_Q19_8_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_8_1E_STATUS));
        EVALUATION_Q19_8_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_8_2E_STATUS));
        EVALUATION_Q19_9_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_9_1E_STATUS));
        EVALUATION_Q19_9_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q19_9_2E_STATUS));
        EVALUATION_Q20_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q20_1E_STATUS));
        EVALUATION_Q20_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q20_2E_STATUS));
        EVALUATION_Q21_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q21_1E_STATUS));
        EVALUATION_Q21_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q21_2E_STATUS));
        EVALUATION_Q21_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q21_3E_STATUS));
        EVALUATION_Q21_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q21_4E_STATUS));
        EVALUATION_Q22_1_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_1_1E_STATUS));
        EVALUATION_Q22_1_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_1_2E_STATUS));
        EVALUATION_Q22_1_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_1_3E_STATUS));
        EVALUATION_Q22_1_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_1_4E_STATUS));
        EVALUATION_Q22_2_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_2_1E_STATUS));
        EVALUATION_Q22_2_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_2_2E_STATUS));
        EVALUATION_Q22_2_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_2_3E_STATUS));
        EVALUATION_Q22_2_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_2_4E_STATUS));
        EVALUATION_Q22_3_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_3_1E_STATUS));
        EVALUATION_Q22_3_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_3_2E_STATUS));
        EVALUATION_Q22_3_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_3_3E_STATUS));
        EVALUATION_Q22_3_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_3_4E_STATUS));
        EVALUATION_Q22_4_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_4_1E_STATUS));
        EVALUATION_Q22_4_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_4_2E_STATUS));
        EVALUATION_Q22_4_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_4_3E_STATUS));
        EVALUATION_Q22_4_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_4_4E_STATUS));
        EVALUATION_Q22_5_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_5_1E_STATUS));
        EVALUATION_Q22_5_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_5_2E_STATUS));
        EVALUATION_Q22_5_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_5_3E_STATUS));
        EVALUATION_Q22_5_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_5_4E_STATUS));
        EVALUATION_Q22_6_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_6_1E_STATUS));
        EVALUATION_Q22_6_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_6_2E_STATUS));
        EVALUATION_Q22_6_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_6_3E_STATUS));
        EVALUATION_Q22_6_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_6_4E_STATUS));
        EVALUATION_Q22_7_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_7_1E_STATUS));
        EVALUATION_Q22_7_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_7_2E_STATUS));
        EVALUATION_Q22_7_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_7_3E_STATUS));
        EVALUATION_Q22_7_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_7_4E_STATUS));
        EVALUATION_Q22_8_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_8_1E_STATUS));
        EVALUATION_Q22_8_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_8_2E_STATUS));
        EVALUATION_Q22_8_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_8_3E_STATUS));
        EVALUATION_Q22_8_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q22_8_4E_STATUS));
        EVALUATION_Q23_1_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_1_1E_STATUS));
        EVALUATION_Q23_1_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_1_2E_STATUS));
        EVALUATION_Q23_1_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_1_3E_STATUS));
        EVALUATION_Q23_1_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_1_4E_STATUS));
        EVALUATION_Q23_2_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_2_1E_STATUS));
        EVALUATION_Q23_2_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_2_2E_STATUS));
        EVALUATION_Q23_2_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_2_3E_STATUS));
        EVALUATION_Q23_2_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_2_4E_STATUS));
        EVALUATION_Q23_3_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_3_1E_STATUS));
        EVALUATION_Q23_3_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_3_2E_STATUS));
        EVALUATION_Q23_3_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_3_3E_STATUS));
        EVALUATION_Q23_3_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_3_4E_STATUS));
        EVALUATION_Q23_4_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_4_1E_STATUS));
        EVALUATION_Q23_4_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_4_2E_STATUS));
        EVALUATION_Q23_4_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_4_3E_STATUS));
        EVALUATION_Q23_4_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_4_4E_STATUS));
        EVALUATION_Q23_5_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_5_1E_STATUS));
        EVALUATION_Q23_5_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_5_2E_STATUS));
        EVALUATION_Q23_5_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_5_3E_STATUS));
        EVALUATION_Q23_5_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_5_4E_STATUS));
        EVALUATION_Q23_6_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_6_1E_STATUS));
        EVALUATION_Q23_6_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_6_2E_STATUS));
        EVALUATION_Q23_6_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_6_3E_STATUS));
        EVALUATION_Q23_6_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_6_4E_STATUS));
        EVALUATION_Q23_7_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_7_1E_STATUS));
        EVALUATION_Q23_7_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_7_2E_STATUS));
        EVALUATION_Q23_7_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_7_3E_STATUS));
        EVALUATION_Q23_7_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_7_4E_STATUS));
        EVALUATION_Q23_8_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_8_1E_STATUS));
        EVALUATION_Q23_8_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_8_2E_STATUS));
        EVALUATION_Q23_8_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_8_3E_STATUS));
        EVALUATION_Q23_8_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q23_8_4E_STATUS));
        EVALUATION_Q24_1_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_1_1E_STATUS));
        EVALUATION_Q24_1_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_1_2E_STATUS));
        EVALUATION_Q24_1_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_1_3E_STATUS));
        EVALUATION_Q24_1_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_1_4E_STATUS));
        EVALUATION_Q24_2_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_2_1E_STATUS));
        EVALUATION_Q24_2_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_2_2E_STATUS));
        EVALUATION_Q24_2_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_2_3E_STATUS));
        EVALUATION_Q24_2_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_2_4E_STATUS));
        EVALUATION_Q24_3_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_3_1E_STATUS));
        EVALUATION_Q24_3_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_3_2E_STATUS));
        EVALUATION_Q24_3_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_3_3E_STATUS));
        EVALUATION_Q24_3_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_3_4E_STATUS));
        EVALUATION_Q24_4_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_4_1E_STATUS));
        EVALUATION_Q24_4_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_4_2E_STATUS));
        EVALUATION_Q24_4_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_4_3E_STATUS));
        EVALUATION_Q24_4_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_4_4E_STATUS));
        EVALUATION_Q24_5_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_5_1E_STATUS));
        EVALUATION_Q24_5_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_5_2E_STATUS));
        EVALUATION_Q24_5_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_5_3E_STATUS));
        EVALUATION_Q24_5_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_5_4E_STATUS));
        EVALUATION_Q24_6_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_6_1E_STATUS));
        EVALUATION_Q24_6_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_6_2E_STATUS));
        EVALUATION_Q24_6_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_6_3E_STATUS));
        EVALUATION_Q24_6_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_6_4E_STATUS));
        EVALUATION_Q24_7_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_7_1E_STATUS));
        EVALUATION_Q24_7_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_7_2E_STATUS));
        EVALUATION_Q24_7_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_7_3E_STATUS));
        EVALUATION_Q24_7_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_7_4E_STATUS));
        EVALUATION_Q24_8_1E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_8_1E_STATUS));
        EVALUATION_Q24_8_2E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_8_2E_STATUS));
        EVALUATION_Q24_8_3E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_8_3E_STATUS));
        EVALUATION_Q24_8_4E_STATUS = finalModel.createProperty(env.get(GSBConstants.EVALUATION_Q24_8_4E_STATUS));
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
    
    
    final Set<String> XMLBASEDLiterals=new HashSet<String>(Arrays.asList(new String[] {"http://www.opengis.net/ont/geosparql#gmlLiteral","http://www.opengis.net/ont/geosparql#kmlLiteral"}));
    
    final Set<String> TEXTBASEDLiterals=new HashSet<String>(Arrays.asList(new String[] {"http://www.opengis.net/ont/geosparql#wktLiteral"}));
    
    final Set<String> JSONBASEDLiterals=new HashSet<String>(Arrays.asList(new String[] {"http://www.opengis.net/ont/geosparql#geoJSONLiteral"}));
    
    private String removeWKTWhiteSpaces(String jsonstr) {
    	org.apache.xml.security.Init.init(); 
        JSONObject jsonobj=new JSONObject(jsonstr);
        if(jsonobj.has("results") && jsonobj.getJSONObject("results").has("bindings")) {
        	JSONArray bindings=jsonobj.getJSONObject("results").getJSONArray("bindings");
        	for(int i=0;i<bindings.length();i++) {
        		JSONObject curbinds=bindings.getJSONObject(i);
        		for(String key:curbinds.keySet()) {
        			JSONObject curbinding=curbinds.getJSONObject(key);
            		if(curbinding.has("datatype") && TEXTBASEDLiterals.contains(curbinding.getString("datatype"))) {
            			curbinding.put("value", curbinding.getString("value").replace(" ", "").replace("\n", "").trim().toLowerCase());
            		}else if(curbinding.has("datatype") && JSONBASEDLiterals.contains(curbinding.getString("datatype"))){
            			try {
                	        ObjectMapper mapper = new ObjectMapper();
                			mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
							curbinding.put("value", mapper.readTree(curbinding.getString("value")).asText());
						} catch (JSONException | JsonProcessingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            		}else if(curbinding.has("datatype") && XMLBASEDLiterals.contains(curbinding.getString("datatype"))) {
            			Canonicalizer canon;
						try {
							canon = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_OMIT_COMMENTS);
	            			ByteArrayOutputStream out=new ByteArrayOutputStream();
	            			canon.canonicalize(curbinding.getString("value").getBytes(),out,true);
	            			String canonXmlString = new String(out.toByteArray());
	            			curbinding.put("value", canonXmlString);
						} catch (InvalidCanonicalizerException | XMLParserException | CanonicalizationException | JSONException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            		}
        		}
        	}
        }
        return jsonobj.toString();
    }

    @Override
    protected void evaluateResponse(byte[] expectedData, byte[] receivedData, long taskSentTimestamp, long responseReceivedTimestamp) throws Exception {
        String eStr = RabbitMQUtils.readString(expectedData);
        String rStr = RabbitMQUtils.readString(receivedData);

        LOGGER.info("Full value of eStr: " + eStr);

        String [] lines = eStr.split("\n\n");
        String queryIndexString = lines[0].trim().substring(3);
        int queryIndex = Integer.parseInt(queryIndexString) - 1;

        eStr = "";
        for (int i=1; i < lines.length; i++) {
            eStr += lines[i];
        }

        LOGGER.info("Value of eStr after first split: " + eStr);

        rStr = removeWKTWhiteSpaces(rStr);

        LOGGER.info("Expected answer (and alternatives) for query " + queryIndexString + ": " + eStr);
        ObjectMapper mapper = new ObjectMapper();

        String [] expectedAnswerAlternatives = eStr.split("======");
        for (int i=0; i < expectedAnswerAlternatives.length; i++) {
            expectedAnswerAlternatives[i] = removeWKTWhiteSpaces(expectedAnswerAlternatives[i]);
            correctAnswers[queryIndex] = mapper.readTree(rStr).equals(mapper.readTree(expectedAnswerAlternatives[i]));//(expectedAnswerAlternatives[i].compareToIgnoreCase(rStr) == 0);
            if(correctAnswers[queryIndex]) {
                LOGGER.info("Correct partial answer on query " + queryIndexString + " (" + i + "/" + (expectedAnswerAlternatives.length - 1) + "): " + expectedAnswerAlternatives[i] + " vs. " + rStr);
                break;
            }
            else {
                LOGGER.info("Wrong partial answer on query " + queryIndexString + " (" + i + "/" + (expectedAnswerAlternatives.length - 1) + "): " + expectedAnswerAlternatives[i] + " vs. " + rStr);
            }
        }
        
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
        Literal q19_1_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[46], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_1_1E_STATUS, q19_1_1eStatusLiteral);
        Literal q19_1_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[47], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_1_2E_STATUS, q19_1_2eStatusLiteral);
        Literal q19_1_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[48], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_1_3E_STATUS, q19_1_3eStatusLiteral);
        Literal q19_1_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[49], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_1_4E_STATUS, q19_1_4eStatusLiteral);
        Literal q19_2_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[50], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_2_1E_STATUS, q19_2_1eStatusLiteral);
        Literal q19_2_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[51], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_2_2E_STATUS, q19_2_2eStatusLiteral);
        Literal q19_3_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[52], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_3_1E_STATUS, q19_3_1eStatusLiteral);
        Literal q19_3_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[53], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_3_2E_STATUS, q19_3_2eStatusLiteral);
        Literal q19_4_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[54], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_4_1E_STATUS, q19_4_1eStatusLiteral);
        Literal q19_4_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[55], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_4_2E_STATUS, q19_4_2eStatusLiteral);
        Literal q19_4_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[56], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_4_3E_STATUS, q19_4_3eStatusLiteral);
        Literal q19_4_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[57], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_4_4E_STATUS, q19_4_4eStatusLiteral);
        Literal q19_5_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[58], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_5_1E_STATUS, q19_5_1eStatusLiteral);
        Literal q19_5_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[59], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_5_2E_STATUS, q19_5_2eStatusLiteral);
        Literal q19_5_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[60], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_5_3E_STATUS, q19_5_3eStatusLiteral);
        Literal q19_5_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[61], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_5_4E_STATUS, q19_5_4eStatusLiteral);
        Literal q19_6_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[62], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_6_1E_STATUS, q19_6_1eStatusLiteral);
        Literal q19_6_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[63], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_6_2E_STATUS, q19_6_2eStatusLiteral);
        Literal q19_6_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[64], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_6_3E_STATUS, q19_6_3eStatusLiteral);
        Literal q19_6_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[65], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_6_4E_STATUS, q19_6_4eStatusLiteral);
        Literal q19_7_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[66], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_7_1E_STATUS, q19_7_1eStatusLiteral);
        Literal q19_7_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[67], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_7_2E_STATUS, q19_7_2eStatusLiteral);
        Literal q19_7_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[68], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_7_3E_STATUS, q19_7_3eStatusLiteral);
        Literal q19_7_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[69], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_7_4E_STATUS, q19_7_4eStatusLiteral);
        Literal q19_8_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[70], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_8_1E_STATUS, q19_8_1eStatusLiteral);
        Literal q19_8_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[71], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_8_2E_STATUS, q19_8_2eStatusLiteral);
        Literal q19_9_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[72], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_9_1E_STATUS, q19_9_1eStatusLiteral);
        Literal q19_9_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[73], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q19_9_2E_STATUS, q19_9_2eStatusLiteral);
        Literal q20_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[74], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q20_1E_STATUS, q20_1eStatusLiteral);
        Literal q20_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[75], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q20_2E_STATUS, q20_2eStatusLiteral);
        Literal q21_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[76], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q21_1E_STATUS, q21_1eStatusLiteral);
        Literal q21_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[77], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q21_2E_STATUS, q21_2eStatusLiteral);
        Literal q21_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[78], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q21_3E_STATUS, q21_3eStatusLiteral);
        Literal q21_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[79], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q21_4E_STATUS, q21_4eStatusLiteral);
        Literal q22_1_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[80], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_1_1E_STATUS, q22_1_1eStatusLiteral);
        Literal q22_1_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[81], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_1_2E_STATUS, q22_1_2eStatusLiteral);
        Literal q22_1_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[82], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_1_3E_STATUS, q22_1_3eStatusLiteral);
        Literal q22_1_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[83], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_1_4E_STATUS, q22_1_4eStatusLiteral);
        Literal q22_2_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[84], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_2_1E_STATUS, q22_2_1eStatusLiteral);
        Literal q22_2_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[85], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_2_2E_STATUS, q22_2_2eStatusLiteral);
        Literal q22_2_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[86], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_2_3E_STATUS, q22_2_3eStatusLiteral);
        Literal q22_2_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[87], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_2_4E_STATUS, q22_2_4eStatusLiteral);
        Literal q22_3_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[88], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_3_1E_STATUS, q22_3_1eStatusLiteral);
        Literal q22_3_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[89], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_3_2E_STATUS, q22_3_2eStatusLiteral);
        Literal q22_3_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[90], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_3_3E_STATUS, q22_3_3eStatusLiteral);
        Literal q22_3_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[91], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_3_4E_STATUS, q22_3_4eStatusLiteral);
        Literal q22_4_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[92], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_4_1E_STATUS, q22_4_1eStatusLiteral);
        Literal q22_4_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[93], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_4_2E_STATUS, q22_4_2eStatusLiteral);
        Literal q22_4_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[94], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_4_3E_STATUS, q22_4_3eStatusLiteral);
        Literal q22_4_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[95], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_4_4E_STATUS, q22_4_4eStatusLiteral);
        Literal q22_5_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[96], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_5_1E_STATUS, q22_5_1eStatusLiteral);
        Literal q22_5_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[97], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_5_2E_STATUS, q22_5_2eStatusLiteral);
        Literal q22_5_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[98], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_5_3E_STATUS, q22_5_3eStatusLiteral);
        Literal q22_5_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[99], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_5_4E_STATUS, q22_5_4eStatusLiteral);
        Literal q22_6_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[100], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_6_1E_STATUS, q22_6_1eStatusLiteral);
        Literal q22_6_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[101], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_6_2E_STATUS, q22_6_2eStatusLiteral);
        Literal q22_6_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[102], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_6_3E_STATUS, q22_6_3eStatusLiteral);
        Literal q22_6_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[103], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_6_4E_STATUS, q22_6_4eStatusLiteral);
        Literal q22_7_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[104], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_7_1E_STATUS, q22_7_1eStatusLiteral);
        Literal q22_7_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[105], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_7_2E_STATUS, q22_7_2eStatusLiteral);
        Literal q22_7_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[106], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_7_3E_STATUS, q22_7_3eStatusLiteral);
        Literal q22_7_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[107], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_7_4E_STATUS, q22_7_4eStatusLiteral);
        Literal q22_8_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[108], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_8_1E_STATUS, q22_8_1eStatusLiteral);
        Literal q22_8_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[109], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_8_2E_STATUS, q22_8_2eStatusLiteral);
        Literal q22_8_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[110], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_8_3E_STATUS, q22_8_3eStatusLiteral);
        Literal q22_8_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[111], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q22_8_4E_STATUS, q22_8_4eStatusLiteral);
        Literal q23_1_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[112], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_1_1E_STATUS, q23_1_1eStatusLiteral);
        Literal q23_1_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[113], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_1_2E_STATUS, q23_1_2eStatusLiteral);
        Literal q23_1_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[114], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_1_3E_STATUS, q23_1_3eStatusLiteral);
        Literal q23_1_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[115], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_1_4E_STATUS, q23_1_4eStatusLiteral);
        Literal q23_2_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[116], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_2_1E_STATUS, q23_2_1eStatusLiteral);
        Literal q23_2_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[117], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_2_2E_STATUS, q23_2_2eStatusLiteral);
        Literal q23_2_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[118], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_2_3E_STATUS, q23_2_3eStatusLiteral);
        Literal q23_2_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[119], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_2_4E_STATUS, q23_2_4eStatusLiteral);
        Literal q23_3_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[120], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_3_1E_STATUS, q23_3_1eStatusLiteral);
        Literal q23_3_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[121], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_3_2E_STATUS, q23_3_2eStatusLiteral);
        Literal q23_3_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[122], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_3_3E_STATUS, q23_3_3eStatusLiteral);
        Literal q23_3_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[123], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_3_4E_STATUS, q23_3_4eStatusLiteral);
        Literal q23_4_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[124], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_4_1E_STATUS, q23_4_1eStatusLiteral);
        Literal q23_4_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[125], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_4_2E_STATUS, q23_4_2eStatusLiteral);
        Literal q23_4_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[126], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_4_3E_STATUS, q23_4_3eStatusLiteral);
        Literal q23_4_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[127], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_4_4E_STATUS, q23_4_4eStatusLiteral);
        Literal q23_5_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[128], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_5_1E_STATUS, q23_5_1eStatusLiteral);
        Literal q23_5_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[129], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_5_2E_STATUS, q23_5_2eStatusLiteral);
        Literal q23_5_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[130], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_5_3E_STATUS, q23_5_3eStatusLiteral);
        Literal q23_5_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[131], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_5_4E_STATUS, q23_5_4eStatusLiteral);
        Literal q23_6_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[132], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_6_1E_STATUS, q23_6_1eStatusLiteral);
        Literal q23_6_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[133], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_6_2E_STATUS, q23_6_2eStatusLiteral);
        Literal q23_6_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[134], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_6_3E_STATUS, q23_6_3eStatusLiteral);
        Literal q23_6_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[135], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_6_4E_STATUS, q23_6_4eStatusLiteral);
        Literal q23_7_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[136], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_7_1E_STATUS, q23_7_1eStatusLiteral);
        Literal q23_7_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[137], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_7_2E_STATUS, q23_7_2eStatusLiteral);
        Literal q23_7_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[138], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_7_3E_STATUS, q23_7_3eStatusLiteral);
        Literal q23_7_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[139], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_7_4E_STATUS, q23_7_4eStatusLiteral);
        Literal q23_8_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[140], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_8_1E_STATUS, q23_8_1eStatusLiteral);
        Literal q23_8_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[141], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_8_2E_STATUS, q23_8_2eStatusLiteral);
        Literal q23_8_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[142], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_8_3E_STATUS, q23_8_3eStatusLiteral);
        Literal q23_8_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[143], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q23_8_4E_STATUS, q23_8_4eStatusLiteral);
        Literal q24_1_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[144], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_1_1E_STATUS, q24_1_1eStatusLiteral);
        Literal q24_1_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[145], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_1_2E_STATUS, q24_1_2eStatusLiteral);
        Literal q24_1_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[146], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_1_3E_STATUS, q24_1_3eStatusLiteral);
        Literal q24_1_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[147], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_1_4E_STATUS, q24_1_4eStatusLiteral);
        Literal q24_2_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[148], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_2_1E_STATUS, q24_2_1eStatusLiteral);
        Literal q24_2_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[149], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_2_2E_STATUS, q24_2_2eStatusLiteral);
        Literal q24_2_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[150], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_2_3E_STATUS, q24_2_3eStatusLiteral);
        Literal q24_2_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[151], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_2_4E_STATUS, q24_2_4eStatusLiteral);
        Literal q24_3_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[152], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_3_1E_STATUS, q24_3_1eStatusLiteral);
        Literal q24_3_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[153], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_3_2E_STATUS, q24_3_2eStatusLiteral);
        Literal q24_3_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[154], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_3_3E_STATUS, q24_3_3eStatusLiteral);
        Literal q24_3_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[155], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_3_4E_STATUS, q24_3_4eStatusLiteral);
        Literal q24_4_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[156], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_4_1E_STATUS, q24_4_1eStatusLiteral);
        Literal q24_4_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[157], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_4_2E_STATUS, q24_4_2eStatusLiteral);
        Literal q24_4_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[158], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_4_3E_STATUS, q24_4_3eStatusLiteral);
        Literal q24_4_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[159], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_4_4E_STATUS, q24_4_4eStatusLiteral);
        Literal q24_5_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[160], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_5_1E_STATUS, q24_5_1eStatusLiteral);
        Literal q24_5_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[161], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_5_2E_STATUS, q24_5_2eStatusLiteral);
        Literal q24_5_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[162], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_5_3E_STATUS, q24_5_3eStatusLiteral);
        Literal q24_5_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[163], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_5_4E_STATUS, q24_5_4eStatusLiteral);
        Literal q24_6_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[164], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_6_1E_STATUS, q24_6_1eStatusLiteral);
        Literal q24_6_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[165], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_6_2E_STATUS, q24_6_2eStatusLiteral);
        Literal q24_6_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[166], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_6_3E_STATUS, q24_6_3eStatusLiteral);
        Literal q24_6_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[167], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_6_4E_STATUS, q24_6_4eStatusLiteral);
        Literal q24_7_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[168], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_7_1E_STATUS, q24_7_1eStatusLiteral);
        Literal q24_7_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[169], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_7_2E_STATUS, q24_7_2eStatusLiteral);
        Literal q24_7_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[170], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_7_3E_STATUS, q24_7_3eStatusLiteral);
        Literal q24_7_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[171], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_7_4E_STATUS, q24_7_4eStatusLiteral);
        Literal q24_8_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[172], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_8_1E_STATUS, q24_8_1eStatusLiteral);
        Literal q24_8_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[173], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_8_2E_STATUS, q24_8_2eStatusLiteral);
        Literal q24_8_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[174], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_8_3E_STATUS, q24_8_3eStatusLiteral);
        Literal q24_8_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[175], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q24_8_4E_STATUS, q24_8_4eStatusLiteral);
        Literal q25_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[176], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q25_1E_STATUS, q25_1eStatusLiteral);
        Literal q25_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[177], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q25_2E_STATUS, q25_2eStatusLiteral);
        Literal q25_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[178], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q25_3E_STATUS, q25_3eStatusLiteral);
        Literal q26_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[179], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q26_1E_STATUS, q26_1eStatusLiteral);
        Literal q26_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[180], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q26_2E_STATUS, q26_2eStatusLiteral);
        Literal q27eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[181], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q27E_STATUS, q27eStatusLiteral);
        Literal q28_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[182], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q28_1E_STATUS, q28_1eStatusLiteral);
        Literal q28_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[183], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q28_2E_STATUS, q28_2eStatusLiteral);
        Literal q28_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[184], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q28_3E_STATUS, q28_3eStatusLiteral);
        Literal q28_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[185], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q28_4E_STATUS, q28_4eStatusLiteral);
        Literal q28_5eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[186], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q28_5E_STATUS, q28_5eStatusLiteral);
        Literal q28_6eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[187], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q28_6E_STATUS, q28_6eStatusLiteral);
        Literal q28_7eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[188], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q28_7E_STATUS, q28_7eStatusLiteral);
        Literal q28_8eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[189], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q28_8E_STATUS, q28_8eStatusLiteral);
        Literal q29_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[190], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q29_1E_STATUS, q29_1eStatusLiteral);
        Literal q29_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[191], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q29_2E_STATUS, q29_2eStatusLiteral);
        Literal q29_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[192], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q29_3E_STATUS, q29_3eStatusLiteral);
        Literal q29_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[193], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q29_4E_STATUS, q29_4eStatusLiteral);
        Literal q29_5eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[194], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q29_5E_STATUS, q29_5eStatusLiteral);
        Literal q29_6eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[195], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q29_6E_STATUS, q29_6eStatusLiteral);
        Literal q29_7eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[196], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q29_7E_STATUS, q29_7eStatusLiteral);
        Literal q29_8eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[197], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q29_8E_STATUS, q29_8eStatusLiteral);
        Literal q30_1eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[198], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q30_1E_STATUS, q30_1eStatusLiteral);
        Literal q30_2eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[199], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q30_2E_STATUS, q30_2eStatusLiteral);
        Literal q30_3eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[200], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q30_3E_STATUS, q30_3eStatusLiteral);
        Literal q30_4eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[201], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q30_4E_STATUS, q30_4eStatusLiteral);
        Literal q30_5eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[202], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q30_5E_STATUS, q30_5eStatusLiteral);
        Literal q30_6eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[203], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q30_6E_STATUS, q30_6eStatusLiteral);
        Literal q30_7eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[204], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q30_7E_STATUS, q30_7eStatusLiteral);
        Literal q30_8eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[205], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q30_8E_STATUS, q30_8eStatusLiteral);
        
        int totalCorrect = 0;
        double percentageCorrect = 0.0;
        for (int i=0; i < correctAnswers.length; i++) {
            if (correctAnswers[i]) {
                totalCorrect += 1;
                percentageCorrect += GSBConstants.GSB_ANSWERS_WEIGHTS[i];
            }
        }

        if (percentageCorrect > 0) {
            percentageCorrect += 1.0 / 30.0; // Adding one 'correct' answer, for Req. 17 which is not tested, but only if the system scores above zero.
        }
        percentageCorrect = percentageCorrect * 100.0; // Transforming the result into a 0-100% range
        
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
