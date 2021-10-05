package org.hobbit.geosparql;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
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

    /* Property for number of correct answers" */
    private Property EVALUATION_NUMBER_OF_CORRECT_ANSWERS = null;

    /* Property for number of correct requirements" */
    private Property EVALUATION_PERCENTAGE_OF_SATISFIED_REQUIREMENTS = null;
	
    private Boolean[] correctAnswers = new Boolean[GSBConstants.GSB_NUMBER_OF_QUERIES];
    //private Boolean[] satisfiedRequirements = new Boolean[GSBConstants.GSB_NUMBER_OF_REQUIREMENTS];
    //private Map<String, ArrayList<Long> > executionStatuses = new HashMap<>();
    
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
        
        for(String key:GSBConstants.GSB_EVALUATION_STATUS.keySet()) {
        	if(!env.containsKey(key)) {
        		 throw new IllegalArgumentException("Couldn't get \"" + key + "\" from the environment. Aborting."); 
        	}
        }
        
        for(String key:GSBConstants.GSB_EVALUATION_STATUS.keySet()) {
        	finalModel.createProperty(env.get(key));
        }
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
							e.printStackTrace();
						}
            		}else if(curbinding.has("datatype") && XMLBASEDLiterals.contains(curbinding.getString("datatype"))) {
            			Canonicalizer canon;
						try {
							canon = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_OMIT_COMMENTS);
	            			ByteArrayOutputStream out=new ByteArrayOutputStream();
	            			canon.canonicalize(curbinding.getString("value").getBytes(),out,false);
	            			String canonXmlString = new String(out.toByteArray());
	            			curbinding.put("value", canonXmlString);
						} catch (InvalidCanonicalizerException | XMLParserException | CanonicalizationException | JSONException | IOException e) {
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
        
        int i=0;
        String[] uriarray=GSBConstants.GSB_EVALUATION_STATUS.keySet().toArray(new String[0]);
        for(Boolean answer:correctAnswers) {
            Literal q01eStatusLiteral = finalModel.createTypedLiteral(answer, XSDDatatype.XSDboolean);
            finalModel.add(experiment, finalModel.createProperty(uriarray[i]), q01eStatusLiteral);
            i++;
        }
        
        int totalCorrect = 0;
        double percentageCorrect = 0.0;
        i=0;
        for (String answer:GSBConstants.GSB_ANSWERS) {
            if (correctAnswers[i]) {
                totalCorrect += 1;
                percentageCorrect +=GSBConstants.GSB_ANSWERS_WEIGHTS.get(answer);
            }
            i++;
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
