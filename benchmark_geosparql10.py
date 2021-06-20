import os
from SPARQLWrapper import SPARQLWrapper, XML, JSON
from xmldiff import main
import difflib
import json
import re
from xml.dom.minidom import parse, parseString

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

def queryEndpoint(endpoint,query,resultFolder, testid):
	sparql = SPARQLWrapper(endpoint)
	sparql.setQuery(query)
	sparql.setReturnFormat(XML)
	try:
		results = sparql.query().convert().toprettyxml(indent="", newl="\n")
		#print(results)
		results=results.replace("\n","").replace(" ","").replace("\t","").replace("distinct=\"false\"ordered=\"true\"","").replace("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"xsi:schemaLocation=\"http://www.w3.org/2001/sw/DataAccess/rf1/result2.xsd\"","")
		compareResults(getResultFilesList(testid),results,testid)
	except:
		print("except")
	
def compareResults(resultlist,queryresult,testid):
	print(testid)
	m = re.search("^(query-r[0-9]+).*", testid)
	reqid=m.group(1)
	negativeResults={"score":False,"res":[]}
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
		print("ComplianceScore: "+str(compliancescore*100)+"%")
	print("Amount of correct tests: "+str(correctScore)+"/"+str(amountOfQueries)+"="+str((correctScore/amountOfQueries)))
	print("ComplianceScore: "+str(compliancescore*100)+"%")
	f = open("result.txt", "w")
	f.write("Amount of correct tests: "+str(correctScore)+"/"+str(amountOfQueries)+"\n")
	f.write("ComplianceScore: "+str(compliancescore*100)+"%\n")
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
		f.write("Extension "+str(ext)+": "+str((curextscore/extmaxscore)*100)+"%\n")
	f.close()

def getResultFilesList(testid):
	results={}
	for file in os.listdir(resultFolder):
		if file.startswith(testid.replace(".rq","")):
			print("File: "+str(file))
			f = open(resultFolder+file.replace(".rq",".srx"), 'r')  
			content = f.read()
			results[file]=parseString(content).toprettyxml(indent="", newl="\n").replace(" distinct=\"false\"","").replace(" ordered=\"true\"","").replace("\n","").replace(" ","")
			f.close()
	return results
i=0			
for name in os.listdir(queryFolder):
	print(name)
	f = open(queryFolder+"/"+name, 'r')  
	content = f.read()
	if name in inference:
		queryEndpoint(curendpointrdfs,content,resultFolder,name)
	else:
		queryEndpoint(curendpoint,content,resultFolder,name)
	f.close()
	i+=1
print(json.dumps(resultMap, indent=2))
print(json.dumps(resultMapReq, indent=2))
f = open("benchmarkresult.json", "w")
f.write(json.dumps(resultMap, indent=2))
f.close()
comparefile.close()
calculateComplianceScore(resultMap)
