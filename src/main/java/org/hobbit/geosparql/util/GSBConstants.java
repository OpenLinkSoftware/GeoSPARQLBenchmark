package org.hobbit.geosparql.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;


public final class GSBConstants {
	
    public static int GSB_NUMBER_OF_QUERIES=0;
    public static int GSB_NUMBER_OF_REQUIREMENTS=0;
    public static String GSB_PATH="";
    public static Map<String,String> GSB_EVALUATION_STATUS=new TreeMap<String,String>();
    public static Map<String,String> GSB_REQUIREMENTS_MAP=new TreeMap<String,String>();
    public static Map<String,List<String>> GSB_EXTENSION_MAP=new TreeMap<String,List<String>>();
    public static List<String> GSB_QUERIES=new LinkedList<String>();
    public static List<String> GSB_ANSWERS=new LinkedList<String>();
    public static Map<String,Double> GSB_ANSWERS_WEIGHTS=new TreeMap<String,Double>();

    public static final String EVALUATION_NUMBER_OF_CORRECT_ANSWERS = "evaluation_number_of_correct_answers";

    public static final String EVALUATION_PERCENTAGE_OF_SATISFIED_REQUIREMENTS = "evaluation_percentage_of_satisfied_requirements";
	
	public GSBConstants(String configfolder,String configfile) throws IOException, URISyntaxException {
	    GSB_EVALUATION_STATUS=new TreeMap<String,String>();
	    GSB_EXTENSION_MAP=new TreeMap<String,List<String>>();
	    GSB_REQUIREMENTS_MAP=new TreeMap<String,String>();
	    GSB_QUERIES=new LinkedList<String>();
	    GSB_ANSWERS=new LinkedList<String>();
	    GSB_ANSWERS_WEIGHTS=new TreeMap<String,Double>();
		GSB_PATH=configfolder;
		String filecontent=readFileAsString(configfile);		
		JSONObject jsonobj=new JSONObject(filecontent);
		JSONObject weights = jsonobj.getJSONObject("reqWeights");
		for(String key:weights.keySet()) {
			GSB_ANSWERS_WEIGHTS.put(key, weights.getDouble(key));
			GSB_ANSWERS.add(key);
		}
		JSONObject reqToURI = jsonobj.getJSONObject("reqToURI");
		for(String key:reqToURI.keySet()) {
			GSB_REQUIREMENTS_MAP.put(key, reqToURI.getString(key));
		}		
		JSONObject extensions = jsonobj.getJSONObject("extensionMap");
		for(String ext: extensions.keySet()) {
			GSB_EXTENSION_MAP.put(ext, new LinkedList<String>());
			JSONArray extarray=extensions.getJSONArray(ext);
			for(int i=0;i<extarray.length();i++) {
				GSB_EXTENSION_MAP.get(ext).add(extarray.getString(i));
			}
		}
		URL res = getClass().getClassLoader().getResource(configfolder);
		System.out.println(res.toURI());
		File folder = Paths.get(res.toURI()).toFile();
		String[] folderfiles=folder.list();
		Arrays.sort(folderfiles);
		GSB_NUMBER_OF_QUERIES=folderfiles.length;
		GSB_NUMBER_OF_REQUIREMENTS=GSB_REQUIREMENTS_MAP.size();
		for(String str:folderfiles) {
			GSB_QUERIES.add(str);
			GSB_EVALUATION_STATUS.put("evaluation_"+str.replace(".srx", "")+"_execution_status","http://w3id.org/bench#"+str.replace(".srx", "")+"Status");
		}
		System.out.println(GSB_QUERIES);
		System.out.println(GSB_EVALUATION_STATUS);
		System.out.println(GSB_ANSWERS);
		System.out.println(GSB_ANSWERS_WEIGHTS);
		System.out.println(GSB_REQUIREMENTS_MAP);
		System.out.println(GSB_EXTENSION_MAP);
		System.out.println(GSB_NUMBER_OF_QUERIES);
		System.out.println(GSB_NUMBER_OF_REQUIREMENTS);
	}
	
	private String readFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		//new GSBConstants("geosparql10_compliance/gsb_queries","geosparql10_compliance.json");
		new GSBConstants("geosparql11_compliance/gsb_queries","geosparql11_compliance.json");
	}
    

    
}