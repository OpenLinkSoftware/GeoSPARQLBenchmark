import os
import sys
from SPARQLWrapper import SPARQLWrapper, XML, JSON
import json
import re
from xml.dom.minidom import parse, parseString
import lxml.etree as ET

resultMap={}

resultMapReq={}

queryResultMap={}

configfile="geosparql10_compliance.json"
if len(sys.argv)>1:
    configfile=sys.argv[1]
  
f = open(configfile, 'r')  
content = f.read()
config=json.loads(content)

inference={}
if "inference" in config:
    inference=config["inference"]

benchmarkname="Benchmark"
if "benchmarkname" in config:
    benchmarkname=config["benchmarkname"]
    
extensionMap={}
if "extensionMap" in config:
    extensionMap=config["extensionMap"]
    
reqWeights={}
if "reqWeights" in config:
    reqWeights=config["reqWeights"]

reqToURI={}
if "reqToURI" in config:
    reqToURI=config["reqToURI"]

queryFolder={}
if "queryFolders" in config:
    queryFolder=config["queryFolders"]

curendpointrdfs="https://api.triplydb.com/datasets/Timo/geosparqlbenchmarkrdfs/services/geosparqlbenchmarkrdfs/sparql"

curendpoint="https://api.triplydb.com/datasets/timohomburg/geosparqlbenchmark/services/geosparqlbenchmark/sparql"

#queryFolder="src/main/resources/gsb_queries/"

#resultFolder="src/main/resources/gsb_answers/"

comparelog="comparelog.txt"

comparefile = open(comparelog, "w")

## Queries a SPARQL endpoint with a given query extracted from the GeoSPARQL Benchmark repository .
#  @param endpoint the endpoint to query
#  @param query the query to execute
#  @param resultFolder the folder including the anticipated query results
#  @param testid the id of the currently to be executed test
def queryEndpoint(endpoint,query,resultFolder, testid):
	sparql = SPARQLWrapper(endpoint)
	sparql.setQuery(query)
	sparql.setReturnFormat(XML)
	try:
		results = sparql.query().convert().toprettyxml(indent="", newl="\n")
		results=replaceCDATA(results)
		results=results.replace("\n","").replace(" ","").replace("\t","").replace("distinct=\"false\"ordered=\"true\"","").replace("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"xsi:schemaLocation=\"http://www.w3.org/2001/sw/DataAccess/rf1/result2.xsd\"","")
		res=compareResults(getResultFilesList(testid,resultFolder),results,testid,query)
		print(testid+": "+str(res))
	except:
		print("except")

## Compares a list of anticipated query results to the retrieved query result from the triple store .
#  @param resultlist the list of anticipated query results
#  @param queryresult the queryresult of the to be tested triple store
#  @param testid the id of the currently to be executed test
def compareResults(resultlist,queryresult,testid,query):
	print(testid)
	m = re.search("^(query-r[0-9]+).*", testid)
	if m==None:
		return False
	reqid=m.group(1)	
	negativeResults={"score":False,"query":query.replace("\n","").replace("\t",""),"res":[]}
	if not reqid in resultMapReq:
		resultMapReq[reqid]={}
	for res in resultlist:
		if resultlist[res]==queryresult:
			resultMap[testid]=True
			resultMapReq[reqid][testid]=True
			negativeResults["res"].append({"expfile":res,"expected":resultlist[res],"acresult":queryresult})
			negativeResults["score"]=True		
			comparefile.write("==="+testid+"===\n")
			comparefile.write(json.dumps(negativeResults,indent=2))
			comparefile.write("\n")
			comparefile.write("======\n")
			return True
		else:
			negativeResults["res"].append({"expfile":res,"expected":resultlist[res],"acresult":queryresult})
	comparefile.write("==="+testid+"===\n")
	comparefile.write(json.dumps(negativeResults,indent=2))
	comparefile.write("\n")
	comparefile.write("======\n")
	resultMapReq[reqid][testid]=False
	resultMap[testid]=False
	return False

def replaceCDATA(xmlstring):
	xmltree=ET.fromstring(xmlstring)
	for elem in xmltree.iter():
		if "literaldatatype" in elem.tag and "<[CDATA[" in elem.text:
			elem.text=elem.text.replace("<[CDATA[","").replace("]]","")
			elem.text=escape_cdata(elem.text)
	return ET.tostring(xmltree, encoding='utf8', method='xml').decode("utf8")	

def escape_cdata(s):
    s = s.replace("&", "&amp;")
    s = s.replace("<", "&lt;")
    s = s.replace(">", "&gt;")
    return s 

## Calculates the compliance score given a map of evaluated query results .
#  @param resultMap a map of query results
def calculateComplianceScore(resultMap):
	correctScore=0
	amountOfQueries=len(resultMap)
	compliancescore=1.0/30.0
	extensionScores={}
	if amountOfQueries==0:
		return 0
	lastNamePrefix=""
	for res in resultMap:
		if resultMap[res]:
			correctScore+=1
	for res in resultMapReq:
		print(res)
		for assessment in resultMapReq[res]:
			percperThisReq=reqWeights[assessment]
			if resultMapReq[res][assessment]:
				compliancescore+=percperThisReq
		print("ComplianceScore: "+str(compliancescore*100)+"%")
	print("Amount of correct tests: "+str(correctScore)+"/"+str(amountOfQueries)+"="+str((correctScore/amountOfQueries)))
	print("ComplianceScore: "+str(round(compliancescore*100,2))+"%")
	f = open("result.txt", "w")
	f.write("Amount of correct tests: "+str(correctScore)+"/"+str(amountOfQueries)+"\n")
	f.write("ComplianceScore: "+str(round(compliancescore*100,2))+"%\n")
	for ext in extensionMap:
		reqlist=extensionMap[ext]
		extmaxscore=0 #len(extensionMap[ext])*percentagePerReq
		curextscore=0
		for req in reqlist:
			#print(resultMapReq)
			if req in resultMapReq:
				#percperThisReq=reqWeights[req] #percentagePerReq/len(resultMapReq[req])
				for assessment in resultMapReq[req]:
					percperThisReq=reqWeights[assessment]
					if resultMapReq[req][assessment]:
						curextscore+=percperThisReq
					extmaxscore+=percperThisReq
			#else:
			#	curextscore+=percentagePerReq
		f.write("Extension "+str(ext)+": "+str(round((curextscore/extmaxscore)*100,2))+"%\n")
	f.close()


def benchmarkResultsToRDF(resultMap):
	ttlstring="@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n@prefix geo: <http://www.opengis.net/ont/geosparql#> .\n@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n@prefix owl: <http://www.w3.org/2002/07/owl#> .\n"
	ttlstring+="geo:evaluation rdf:type owl:DatatypeProperty .\n"
	ttlstring+="geo:hasEvaluationQuery rdf:type owl:ObjectProperty .\n"
	for ext in extensionMap:
		reqlist=extensionMap[ext]
		extmaxscore=0
		curextscore=0
		for req in reqlist:
			ttlstring+="<"+reqToURI[req]+"> rdf:type geo:Requirement .\n"
			ttlstring+="<"+reqToURI[req]+"_eval> rdf:type geo:RequirementEvaluation .\n"
			#print(resultMapReq)
			if req in resultMapReq:
				#percperThisReq=reqWeights[req]#percentagePerReq/len(resultMapReq[req])
				asscounter=1
				for assessment in resultMapReq[req]:
					percperThisReq=reqWeights[assessment]
					ttlstring+="<"+reqToURI[req]+"_eval> geo:hasEvaluationQuery <"+reqToURI[req]+"/query_"+str(asscounter)+"> .\n"
					ttlstring+="<"+reqToURI[req]+"/query_"+str(asscounter)+"> rdf:type geo:RequirementQuery .\n"
					ttlstring+="<"+reqToURI[req]+"/query_"+str(asscounter)+"> rdfs:label \""+str(reqToURI[req])+" query "+str(asscounter)+"\"@en .\n"
					ttlstring+="<"+reqToURI[req]+"/query_"+str(asscounter)+"_eval> rdf:type geo:RequirementQueryEvaluation .\n"
					ttlstring+="<"+reqToURI[req]+"/query_"+str(asscounter)+"_eval> geo:evaluation \""+str(resultMapReq[req][assessment])+"\"^^xsd:boolean .\n"
					asscounter+=1
					if resultMapReq[req][assessment]:
						curextscore+=percperThisReq
					extmaxscore+=percperThisReq
			#else:
			#	curextscore+=percentagePerReq
		ttlstring+="<"+reqToURI[req]+"_eval> geo:evaluation \""+str(curextscore)+"\"^^xsd:double .\n"
	f = open("benchmarkresult.ttl", "w")
	f.write(ttlstring)
	f.close()

## Retrieves a list of anticipated query results from the repository for a given testid .
#  @param testid the testid to retrieve anticipated results for
def getResultFilesList(testid,resultFolder):
	results={}
	for file in os.listdir(resultFolder):
		if file.startswith(testid.replace(".rq","")):
			#print("File: "+str(file))
			f = open(resultFolder+file.replace(".rq",".srx"), 'r')  
			content = f.read()
			results[file]=parseString(content).toprettyxml(indent="", newl="\n")
			results[file]=replaceCDATA(results[file])
			results[file]=results[file].replace(" distinct=\"false\"","").replace(" ordered=\"true\"","").replace("\n","").replace(" ","")
			f.close()
	return results

for folder in queryFolder.keys():
    for name in os.listdir(folder):
        print(name)
        f = open(folder+"/"+name, 'r')  
        content = f.read()
        print(queryFolder[folder])
        if name in inference:
            queryEndpoint(curendpointrdfs,content,queryFolder[folder],name)
        else:
            queryEndpoint(curendpoint,content,queryFolder[folder],name)
        f.close()
#print(json.dumps(resultMap, indent=2))
#print(json.dumps(resultMapReq, indent=2))
f = open("benchmarkresult.json", "w")
f.write(json.dumps(resultMap, indent=2))
f.close()
comparefile.close()
calculateComplianceScore(resultMap)
benchmarkResultsToRDF(resultMap)
