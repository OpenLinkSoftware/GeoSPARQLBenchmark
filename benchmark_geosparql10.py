import os
from SPARQLWrapper import SPARQLWrapper, XML, JSON
import json
import re
from xml.dom.minidom import parse, parseString
import lxml.etree as ET

resultMap={}

resultMapReq={}

queryResultMap={}

percentagePerReq=0.0333

inference=["query-r25-1.rq","query-r25-2.rq","query-r25-3.rq","query-r26-1.rq","query-r26-2.rq","query-r27.rq"]

benchmarkname="GeoSPARQL 1.0 Compliance Benchmark"

extensionMap={"CORE":["query-r01","query-r02","query-r03"],
			  "TOP":["query-r04","query-r05","query-r06"],
			  "GEOEXT":["query-r07","query-r08","query-r09","query-r10","query-r11","query-r12","query-r13","query-r14","query-r15","query-r16","query-r17","query-r18","query-r19","query-r20"],
			  "GTOP":["query-r21","query-r22","query-r23","query-r24"],
			  "RDFSE":["query-r25","query-r26","query-r27"],
			  "QRW":["query-r28","query-r29","query-r30"]}

curendpointrdfs="https://api.triplydb.com/datasets/Timo/geosparqlbenchmarkrdfs/services/geosparqlbenchmarkrdfs/sparql"

curendpoint="https://api.triplydb.com/datasets/timohomburg/geosparqlbenchmark/services/geosparqlbenchmark/sparql"

queryFolder="src/main/resources/gsb_queries/"

resultFolder="src/main/resources/gsb_answers/"

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
		compareResults(getResultFilesList(testid),results,testid,query)
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
	compliancescore=0
	extensionScores={}
	if amountOfQueries==0:
		return 0
	lastNamePrefix=""
	for res in resultMap:
		if resultMap[res]:
			correctScore+=1
	for res in resultMapReq:
		percperThisReq=percentagePerReq/len(resultMapReq[res])
		for assessment in resultMapReq[res]:
			if resultMapReq[res][assessment]:
				compliancescore+=percperThisReq
		print("ComplianceScore: "+str(round(compliancescore*100,2))+"%")
	print("Amount of correct tests: "+str(correctScore)+"/"+str(amountOfQueries)+"="+str((correctScore/amountOfQueries)))
	print("ComplianceScore: "+str(round(compliancescore*100,2))+"%")
	f = open("result.txt", "w")
	f.write("Amount of correct tests: "+str(correctScore)+"/"+str(amountOfQueries)+"\n")
	f.write("ComplianceScore: "+str(round(compliancescore*100,2))+"%\n")
	for ext in extensionMap:
		reqlist=extensionMap[ext]
		extmaxscore=len(extensionMap[ext])*percentagePerReq
		curextscore=0
		for req in reqlist:
			print(resultMapReq)
			if req in resultMapReq:
				percperThisReq=percentagePerReq/len(resultMapReq[req])
				for assessment in resultMapReq[req]:
					if resultMapReq[req][assessment]:
						curextscore+=percperThisReq
			else:
				curextscore+=percentagePerReq
		f.write("Extension "+str(ext)+": "+str(round((curextscore/extmaxscore)*100,2))+"%\n")
	f.close()

## Retrieves a list of anticipated query results from the repository for a given testid .
#  @param testid the testid to retrieve anticipated results for
def getResultFilesList(testid):
	results={}
	for file in os.listdir(resultFolder):
		if file.startswith(testid.replace(".rq","")):
			print("File: "+str(file))
			f = open(resultFolder+file.replace(".rq",".srx"), 'r')  
			content = f.read()
			results[file]=parseString(content).toprettyxml(indent="", newl="\n")
			results[file]=replaceCDATA(results[file])
			results[file]=results[file].replace(" distinct=\"false\"","").replace(" ordered=\"true\"","").replace("\n","").replace(" ","")
			f.close()
	return results


for name in os.listdir(queryFolder):
	print(name)
	f = open(queryFolder+"/"+name, 'r')  
	content = f.read()
	if name in inference:
		queryEndpoint(curendpointrdfs,content,resultFolder,name)
	else:
		queryEndpoint(curendpoint,content,resultFolder,name)
	f.close()
print(json.dumps(resultMap, indent=2))
print(json.dumps(resultMapReq, indent=2))
f = open("benchmarkresult.json", "w")
f.write(json.dumps(resultMap, indent=2))
f.close()
comparefile.close()
calculateComplianceScore(resultMap)
