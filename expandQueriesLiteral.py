import os
import itertools


def replaceInString(toreplace, firstliteral, secondliteral,firstliteralrel,secondliteralrel):
	return toreplace.replace("%%literal1%%",firstliteral).replace("%%literal2%%",secondliteral).replace("%%literalrel1%%",firstliteralrel).replace("%%literalrel2%%",secondliteralrel)

querypath = 'src/main/resources/geosparql11_compliance/gsb_querytemplates/'

answerpath = 'src/main/resources/geosparql11_compliance/gsb_answertemplates/'

amountOfRequirements=38

geom_literals = {
    "WKT":"geo:asWKT",
	"GML":"geo:asGML",
    "GeoJSON":"geo:asGeoJSON",
    "KML":"geo:asKML",
	"DGGS":"geo:asDGGS"
}

geom_literals2 = [
    ("WKT","geo:asWKT"),
	("GML","geo:asGML"),
    ("GeoJSON","geo:asGeoJSON"),
    ("KML","geo:asKML"),
	("DGGS","geo:asDGGS")
]

combinations=list(itertools.permutations(geom_literals,2))
combinations+=list(map(lambda x, y:(x,y), geom_literals.keys(), geom_literals.keys()))

for comb in combinations:
    print(comb)

files = os.listdir(querypath)
answerp = os.listdir(answerpath)
gsbconstants="""package org.hobbit.geosparql.util;

public final class GSBConstants {

    public static final String EVALUATION_NUMBER_OF_CORRECT_ANSWERS = "evaluation_number_of_correct_answers";

    public static final String EVALUATION_PERCENTAGE_OF_SATISFIED_REQUIREMENTS = "evaluation_percentage_of_satisfied_requirements";

"""
gsbqueries="""public static final String [] GSB_QUERIES = {"""
gsbanswers=""""""
gsbevalstatus=""""""
gsbevalweights=""""""
gsbevalsummary=""""""
gsbevalweightslist="""public static final double [] GSB_ANSWERS_WEIGHTS = {"""
evalmodulejavaconstructor=""
evalmodulejava="""package org.hobbit.geosparql;

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
    private Model finalModel = ModelFactory.createDefaultModel();\n"""
first=True
gsbquerycounter=0
curreqcounter=0
curreqamount=0
reqstring=""
for f in files:
	if not os.path.isfile(querypath+f):
		continue
	file = open(querypath+f, "r")
	filecontent=file.read()
	fileprefix=f[0:f.rfind("-")]
	if curreqcounter==0:
		curreqamount=0
	if reqstring=="" or reqstring==fileprefix:
		curreqcounter+=1
		curreqamount+=1
	elif curreqamount==0:
		#gsbevalweights+="\tpublic static final double GSB_ANSWER_WEIGHT_"+f.replace(".rq","").replace("query-r","").replace("-","_")+" = 1.0/"+str(amountOfRequirements)+";\n"
		curreqcounter=0
	else:
		curreqcounter=0
	reqstring=fileprefix
	answerfiles={}
	for ans in answerp:
		if fileprefix in ans:
			print(ans+" - "+fileprefix)
			ansfile = open(answerpath+ans, "r")
			filec=ansfile.read()
			answerfiles[ans]=filec
	print(answerfiles)
	variantcounter=1
	if not "%%literal1%%" in filecontent and not "%%literal2%%" in filecontent:
		with open(querypath+"result/"+f, "w") as f2:
			f2.write(filecontent)
		gsbconstants+="\tpublic static final String GSB_QUERY_"+f.replace(".rq","").replace("query-r","").replace("-","_")+" = \""+f+"\";\n"
		gsbanswers+="\tpublic static final String GSB_ANSWER_"+f.replace(".rq","").replace("query-r","").replace("-","_")+" = \""+f.replace(".rq",".srx")+"\";\n"
		if curreqcounter==0:
			for i in range(curreqamount):
				gsbevalweights+="\tpublic static final double GSB_ANSWER_WEIGHT_"+f.replace(".rq","").replace("query-r","").replace("-","_")+" = 1.0/"+str(amountOfRequirements)+".0*1.0/"+str(curreqamount)+";\n"
				gsbevalweightslist+="\tGSB_ANSWER_WEIGHT_"+f.replace(".rq","").replace("query-r","").replace("-","_")+","
		curreqamount=0
		gsbqueries+="GSB_QUERY_"+f.replace(".rq","").replace("query-r","").replace("-","_")+","
		gsbevalstatus+="public static final String EVALUATION_Q"+f.replace(".rq","").replace("query-r","").replace("-","_")+"_STATUS = \"evaluation_"+f.replace(".rq","").replace("query-r","query").replace("-","_")+"_execution_status\";\n"
		if gsbquerycounter%10==0:
			gsbqueries+="\n"
		evalmodulejava+="\tprivate Property EVALUATION_Q"+f.replace(".rq","").replace("query-r","").replace("-","_")+"E_STATUS = null;\n"
		evalmodulejavaconstructor+="""\t\tif (!env.containsKey(GSBConstants.EVALUATION_Q"""+f.replace(".rq","").replace("query-r","").replace("-","_")+"""E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get " + GSBConstants.EVALUATION_Q"""+f.replace(".rq","").replace("query-r","").replace("-","_")+"""E_STATUS+ " from the environment. Aborting.");
		}\n"""
		gsbevalsummary+="""\t\tLiteral q"""+f.replace(".rq","").replace("query-r","").replace("-","_")+"""eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[0], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q"""+f.replace(".rq","").replace("query-r","").replace("-","_")+"""E_STATUS, q01eStatusLiteral);\n"""
		file.close()
		continue
	if "%%literal2%%" in filecontent:
		for lit in combinations:
			#print(lit[0]+" "+lit[1]+" "+geom_literals[lit[0]]+" "+geom_literals[lit[1]])
			newfile=replaceInString(filecontent,lit[0],lit[1],geom_literals[lit[0]],geom_literals[lit[1]])
			with open(querypath+"result/"+f[0:f.rfind("-")]+"-"+str(variantcounter)+".rq", "w") as f2:
				f2.write(newfile)
			evalmodulejava+="\tprivate Property EVALUATION_Q"+f[0:f.rfind("-")].replace("query-r","").replace("-","_")+"_"+str(variantcounter)+"E_STATUS = null;\n"
			evalmodulejavaconstructor+="""\t\tif (!env.containsKey(GSBConstants.EVALUATION_Q"""+f[0:f.rfind("-")].replace("query-r","").replace("-","_")+"_"+str(variantcounter)+"""E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get " + GSBConstants.EVALUATION_Q"""+f[0:f.rfind("-")].replace("query-r","").replace("-","_")+"_"+str(variantcounter)+"""E_STATUS+ " from the environment. Aborting.");
		}\n"""
			gsbevalsummary+="""\t\tLiteral q"""+f[0:f.rfind("-")].replace("query-r","").replace("-","_")+"_"+str(variantcounter)+"""eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[0], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q"""+f[0:f.rfind("-")].replace("query-r","").replace("-","_")+"_"+str(variantcounter)+"""E_STATUS, q01eStatusLiteral);\n"""
			gsbconstants+="\tpublic static final String GSB_QUERY_"+f[0:f.rfind("-")].replace("query-r","").replace("-","_")+"_"+str(variantcounter)+" = \""+f[0:f.rfind("-")]+"-"+str(variantcounter)+".rq\";\n"
			gsbqueries+="GSB_QUERY_"+f.replace(".rq","").replace("query-r","").replace("-","_")+","
			gsbevalstatus+="public static final String EVALUATION_Q"+f.replace(".rq","").replace("query-r","").replace("-","_")+"_"+str(variantcounter)+"_STATUS = \"evaluation_"+f.replace(".rq","").replace("query-r","query").replace("-","_")+"_execution_status\";\n"
			gsbevalweights+="\tpublic static final double GSB_ANSWER_WEIGHT_"+f[0:f.rfind("-")].replace("query-r","").replace("-","_")+"_"+str(variantcounter)+" = 1.0/"+str(amountOfRequirements)+".0*1.0/"+str(len(combinations))+".0;\n"
			gsbevalweightslist+="GSB_ANSWER_WEIGHT_"+f[0:f.rfind("-")].replace("query-r","").replace("-","_")+"_"+str(variantcounter)+",\n"
			gsbanswers+="\tpublic static final String GSB_ANSWER_"+f[0:f.rfind("-")].replace("query-r","").replace("-","_")+"_"+str(variantcounter)+" = \""+f[0:f.rfind("-")]+"-"+str(variantcounter)+".srx\";\n"
			if gsbquerycounter%10==0:
				gsbqueries+="\n"
			gsbquerycounter+=1
			first=True    
			answercounter=0
			for ans in answerfiles:
				if first:
					with open(answerpath+"result/"+f[0:f.rfind("-")]+"-"+str(variantcounter)+".srx", "w") as f2:
						f2.write(answerfiles[ans])
					first=False                    
				else:
					with open(answerpath+"result/"+f[0:f.rfind("-")]+"-"+str(variantcounter)+"-alternative-"+str(answercounter)+".srx", "w") as f2:
						f2.write(answerfiles[ans])					
				answercounter+=1                    
			variantcounter=variantcounter+1
		file.close()
	else:	
		for lit in geom_literals2:
			print(lit[0]+" "+lit[1])
			newfile=replaceInString(filecontent,lit[0],lit[0],lit[1],lit[1])
			with open(querypath+"result/"+f[0:f.rfind("-")]+"-"+str(variantcounter)+".rq", "w") as f2:
				f2.write(newfile)
			evalmodulejava+="\tprivate Property EVALUATION_Q"+f[0:f.rfind("-")].replace("query-r","").replace("-","_")+"_"+str(variantcounter)+"E_STATUS = null;\n"
			evalmodulejavaconstructor+="""\t\tif (!env.containsKey(GSBConstants.EVALUATION_Q"""+f[0:f.rfind("-")].replace("query-r","").replace("-","_")+"_"+str(variantcounter)+"""E_STATUS)) {
            throw new IllegalArgumentException("Couldn't get " + GSBConstants.EVALUATION_Q"""+f[0:f.rfind("-")].replace("query-r","").replace("-","_")+"_"+str(variantcounter)+"""E_STATUS+ " from the environment. Aborting.");
		}\n"""
			gsbevalsummary+="""\t\tLiteral q"""+f[0:f.rfind("-")].replace("query-r","").replace("-","_")+"_"+str(variantcounter)+"""eStatusLiteral = finalModel.createTypedLiteral(correctAnswers[0], XSDDatatype.XSDboolean);
        finalModel.add(experiment, EVALUATION_Q"""+f[0:f.rfind("-")].replace("query-r","").replace("-","_")+"_"+str(variantcounter)+"""E_STATUS, q01eStatusLiteral);\n"""
			gsbconstants+="\tpublic static final String GSB_QUERY_"+f[0:f.rfind("-")].replace("query-r","").replace("-","_")+"_"+str(variantcounter)+" = \""+f[0:f.rfind("-")]+"-"+str(variantcounter)+".rq\";\n"
			gsbqueries+="GSB_QUERY_"+f.replace(".rq","").replace("query-r","").replace("-","_")+","
			gsbevalweights+="\tpublic static final double GSB_ANSWER_WEIGHT_"+f[0:f.rfind("-")].replace("query-r","").replace("-","_")+"_"+str(variantcounter)+" = 1.0/"+str(amountOfRequirements)+".0*1.0/"+str(len(geom_literals2))+".0;\n"
			gsbevalweightslist+="GSB_ANSWER_WEIGHT_"+f[0:f.rfind("-")].replace("query-r","").replace("-","_")+"_"+str(variantcounter)+",\n"
			gsbevalstatus+="public static final String EVALUATION_Q"+f.replace(".rq","").replace("query-r","").replace("-","_")+"_"+str(variantcounter)+"_STATUS = \"evaluation_"+f.replace(".rq","").replace("query-r","query").replace("-","_")+"_execution_status\";\n"
			gsbanswers+="\tpublic static final String GSB_ANSWER_"+f[0:f.rfind("-")].replace("query-r","").replace("-","_")+"_"+str(variantcounter)+" = \""+f[0:f.rfind("-")]+"-"+str(variantcounter)+".srx\";\n"
			if gsbquerycounter%10==0:
				gsbqueries+="\n"
			gsbquerycounter+=1
			variantcounter=variantcounter+1
		file.close()
with open(querypath+"result/GSBConstants.java", "w") as f2:
	f2.write(gsbconstants)
	f2.write("\n\tpublic static final int GSB_NUMBER_OF_QUERIES = "+str(gsbquerycounter)+";\n")
	f2.write(gsbqueries+"};\n")
	f2.write(gsbanswers)
	f2.write(gsbevalstatus)
	f2.write(gsbevalweights)
	f2.write(gsbevalweightslist+"};\n}\n")
with open(querypath+"result/GSBEvaluationModule.java", "w") as f2:
	f2.write(evalmodulejava)
	f2.write("""/* Property for number of correct answers" */
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
	""")
	f2.write(evalmodulejavaconstructor+"\t}")
	f2.write("""    final Set<String> XMLBASEDLiterals=new HashSet<String>(Arrays.asList(new String[] {"http://www.opengis.net/ont/geosparql#gmlLiteral","http://www.opengis.net/ont/geosparql#kmlLiteral"}));
    
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
		""")
	f2.write(gsbevalsummary)
	f2.write("""        
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

}""")
	
	
