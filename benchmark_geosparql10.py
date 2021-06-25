import os
from SPARQLWrapper import SPARQLWrapper, XML, JSON
import json
import re
from xml.dom.minidom import parse, parseString
import lxml.etree as ET

resultMap={}

resultMapReq={}

queryResultMap={}

inference=["query-r25-1.rq","query-r25-2.rq","query-r25-3.rq","query-r26-1.rq","query-r26-2.rq","query-r27.rq"]

benchmarkname="GeoSPARQL 1.0 Compliance Benchmark"

extensionMap={"CORE":["query-r01","query-r02","query-r03"],
			  "TOP":["query-r04","query-r05","query-r06"],
			  "GEOEXT":["query-r07","query-r08","query-r09","query-r10","query-r11","query-r12","query-r13","query-r14","query-r15","query-r16","query-r17","query-r18","query-r19","query-r20"],
			  "GTOP":["query-r21","query-r22","query-r23","query-r24"],
			  "RDFSE":["query-r25","query-r26","query-r27"],
			  "QRW":["query-r28","query-r29","query-r30"]}

reqWeights={
    "query-r01.rq":1.0/30.0,
	"query-r02.rq":1.0/30.0,
	"query-r03.rq":1.0/30.0,
	"query-r04-1.rq":1.0/30.0 * 1.0/8.0,
	"query-r04-2.rq":1.0/30.0 * 1.0/8.0,
	"query-r04-3.rq":1.0/30.0 * 1.0/8.0,
	"query-r04-4.rq":1.0/30.0 * 1.0/8.0,
    "query-r04-5.rq":1.0/30.0 * 1.0/8.0,
	"query-r04-6.rq":1.0/30.0 * 1.0/8.0,
	"query-r04-7.rq":1.0/30.0 * 1.0/8.0,
	"query-r04-8.rq":1.0/30.0 * 1.0/8.0,
	"query-r05-1.rq":1.0/30.0 * 1.0/8.0,
	"query-r05-2.rq":1.0/30.0 * 1.0/8.0,
	"query-r05-3.rq":1.0/30.0 * 1.0/8.0,
	"query-r05-4.rq":1.0/30.0 * 1.0/8.0,
    "query-r05-5.rq":1.0/30.0 * 1.0/8.0,
	"query-r05-6.rq":1.0/30.0 * 1.0/8.0,
	"query-r05-7.rq":1.0/30.0 * 1.0/8.0,
	"query-r05-8.rq":1.0/30.0 * 1.0/8.0,
	"query-r06-1.rq":1.0/30.0 * 1.0/8.0,
	"query-r06-2.rq":1.0/30.0 * 1.0/8.0,
	"query-r06-3.rq":1.0/30.0 * 1.0/8.0,
	"query-r06-4.rq":1.0/30.0 * 1.0/8.0,
    "query-r06-5.rq":1.0/30.0 * 1.0/8.0,
	"query-r06-6.rq":1.0/30.0 * 1.0/8.0,
	"query-r06-7.rq":1.0/30.0 * 1.0/8.0,
	"query-r06-8.rq":1.0/30.0 * 1.0/8.0,
	"query-r07.rq":1.0/30.0,
	"query-r08-1.rq":1.0/30.0 * 1.0/2.0,
	"query-r08-2.rq":1.0/30.0 * 1.0/2.0,
	"query-r09-1.rq":1.0/30.0 * 1.0/6.0,
    "query-r09-2.rq":1.0/30.0 * 1.0/6.0,
	"query-r09-3.rq":1.0/30.0 * 1.0/6.0,
	"query-r09-4.rq":1.0/30.0 * 1.0/6.0,
	"query-r09-5.rq":1.0/30.0 * 1.0/6.0,
	"query-r09-6.rq":1.0/30.0 * 1.0/6.0,
	"query-r10.rq":1.0/30.0,
	"query-r11.rq":1.0/30.0,
	"query-r12.rq":1.0/30.0,
	"query-r13-1.rq":1.0/30.0 * 1.0/2.0,
	"query-r13-2.rq":1.0/30.0 * 1.0/2.0,
	"query-r14.rq":1.0/30.0,
	"query-r15.rq":1.0/30.0,
	"query-r16-1.rq":1.0/30.0 * 1.0/2.0,
	"query-r16-2.rq":1.0/30.0 * 1.0/2.0,
	"query-r18.rq":1.0/30.0,
	"query-r19-1-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r19-1-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r19-1-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r19-1-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r19-2-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/2.0,
	"query-r19-2-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/2.0,
	"query-r19-3-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/2.0,
	"query-r19-3-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/2.0,
	"query-r19-4-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r19-4-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r19-4-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r19-4-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r19-5-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r19-5-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r19-5-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r19-5-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r19-6-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r19-6-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r19-6-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r19-6-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r19-7-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r19-7-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r19-7-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r19-7-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r19-8-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/2.0,
	"query-r19-8-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/2.0,
	"query-r19-9-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/2.0,
	"query-r19-9-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/2.0,
	"query-r20-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/2.0,
	"query-r20-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/2.0,
	"query-r21-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r21-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r21-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r21-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r22-1-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r22-1-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r22-1-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r22-1-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r22-2-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r22-2-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r22-2-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r22-2-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r22-3-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r22-3-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r22-3-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r22-3-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r22-4-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r22-4-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r22-4-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r22-4-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r22-5-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r22-5-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r22-5-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r22-5-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r22-6-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r22-6-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r22-6-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r22-6-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r22-7-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r22-7-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r22-7-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r22-7-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r22-8-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r22-8-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r22-8-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r22-8-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r23-1-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r23-1-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r23-1-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r23-1-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r23-2-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r23-2-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r23-2-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r23-2-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r23-3-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r23-3-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r23-3-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r23-3-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r23-4-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r23-4-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r23-4-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r23-4-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r23-5-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r23-5-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r23-5-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r23-5-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r23-6-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r23-6-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r23-6-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r23-6-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r23-7-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r23-7-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r23-7-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r23-7-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r23-8-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r23-8-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r23-8-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r23-8-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r24-1-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r24-1-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r24-1-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r24-1-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r24-2-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r24-2-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r24-2-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r24-2-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r24-3-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r24-3-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r24-3-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r24-3-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r24-4-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r24-4-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r24-4-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r24-4-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r24-5-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r24-5-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r24-5-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r24-5-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r24-6-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r24-6-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r24-6-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r24-6-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r24-7-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r24-7-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r24-7-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r24-7-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r24-8-1.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r24-8-2.rq":1.0/30.0 * 1.0/9.0 * 1.0/3.0,
	"query-r24-8-3.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r24-8-4.rq":1.0/30.0 * 1.0/9.0 * 1.0/6.0,
	"query-r25-1.rq":1.0/30.0 * 1.0/3.0,
	"query-r25-2.rq":1.0/30.0 * 1.0/3.0,
	"query-r25-3.rq":1.0/30.0 * 1.0/3.0,
	"query-r26-1.rq":1.0/30.0 * 1.0/2.0,
	"query-r26-2.rq":1.0/30.0 * 1.0/2.0,
	"query-r27.rq":1.0/30.0,
	"query-r28-1.rq":1.0/30.0 * 1.0/8.0,
	"query-r28-2.rq":1.0/30.0 * 1.0/8.0,
	"query-r28-3.rq":1.0/30.0 * 1.0/8.0,
	"query-r28-4.rq":1.0/30.0 * 1.0/8.0,
	"query-r28-5.rq":1.0/30.0 * 1.0/8.0,
	"query-r28-6.rq":1.0/30.0 * 1.0/8.0,
	"query-r28-7.rq":1.0/30.0 * 1.0/8.0,
	"query-r28-8.rq":1.0/30.0 * 1.0/8.0,
	"query-r29-1.rq":1.0/30.0 * 1.0/8.0,
	"query-r29-2.rq":1.0/30.0 * 1.0/8.0,
	"query-r29-3.rq":1.0/30.0 * 1.0/8.0,
	"query-r29-4.rq":1.0/30.0 * 1.0/8.0,
	"query-r29-5.rq":1.0/30.0 * 1.0/8.0,
	"query-r29-6.rq":1.0/30.0 * 1.0/8.0,
	"query-r29-7.rq":1.0/30.0 * 1.0/8.0,
	"query-r29-8.rq":1.0/30.0 * 1.0/8.0,
	"query-r30-1.rq":1.0/30.0 * 1.0/8.0,
	"query-r30-2.rq":1.0/30.0 * 1.0/8.0,
	"query-r30-3.rq":1.0/30.0 * 1.0/8.0,
	"query-r30-4.rq":1.0/30.0 * 1.0/8.0,
	"query-r30-5.rq":1.0/30.0 * 1.0/8.0,
	"query-r30-6.rq":1.0/30.0 * 1.0/8.0,
	"query-r30-7.rq":1.0/30.0 * 1.0/8.0,
	"query-r30-8.rq":1.0/30.0 * 1.0/8.0
}


reqToURI={
"query-r01":"http://www.opengis.net/spec/geosparql/1.0/req/core/sparql-protocol",
"query-r02":"http://www.opengis.net/spec/geosparql/1.0/req/core/spatial-object-class",
"query-r03":"http://www.opengis.net/spec/geosparql/1.0/req/core/feature-class",
"query-r04":"http://www.opengis.net/spec/geosparql/1.0/req/topology-vocab-extension/sf-spatial-relations",
"query-r05":"http://www.opengis.net/spec/geosparql/1.0/req/topology-vocab-extension/eh-spatial-relations",
"query-r06":"http://www.opengis.net/spec/geosparql/1.0/req/topology-vocab-extension/rcc8-spatial-relations",
"query-r07":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-extension/geometry-class",
"query-r08":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-extension/feature-properties",
"query-r09":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-extension/geometry-properties",
"query-r10":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-extension/wkt-literal",
"query-r11":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-extension/wkt-literal-default-srs",
"query-r12":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-extension/wkt-axis-order",
"query-r13":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-extension/wkt-literal-empty",
"query-r14":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-extension/geometry-as-wkt-literal",
"query-r15":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-extension/gml-literal",
"query-r16":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-extension/gml-literal-empty",
"query-r17":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-extension/gml-profile",
"query-r18":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-extension/geometry-as-gml-literal",
"query-r19":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-extension/query-functions",
"query-r20":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-extension/srid-function",
"query-r21":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-topology-extension/relate-query-function",
"query-r22":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-topology-extension/sf-query-functions",
"query-r23":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-topology-extension/eh-query-functions",
"query-r24":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-topology-extension/rcc8-query-functions",
"query-r25":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-topology-extension/bgp-rdfs-ent",
"query-r26":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-topology-extension/wkt-geometry-types",
"query-r27":"http://www.opengis.net/spec/geosparql/1.0/req/geometry-topology-extension/gml-geometry-types",
"query-r28":"http://www.opengis.net/spec/geosparql/1.0/req/query-rewrite-extension/sf-query-rewrite",
"query-r29":"http://www.opengis.net/spec/geosparql/1.0/req/query-rewrite-extension/eh-query-rewrite",
"query-r30":"http://www.opengis.net/spec/geosparql/1.0/req/query-rewrite-extension/rcc8-query-rewrite"
}

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
	compliancescore=0.01
	extensionScores={}
	if amountOfQueries==0:
		return 0
	lastNamePrefix=""
	for res in resultMap:
		if resultMap[res]:
			correctScore+=1
	for res in resultMapReq:
		 #percentagePerReq/len(resultMapReq[res])
		for assessment in resultMapReq[res]:
			percperThisReq=reqWeights[assessment]
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
def getResultFilesList(testid):
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


for name in os.listdir(queryFolder):
	print(name)
	f = open(queryFolder+"/"+name, 'r')  
	content = f.read()
	if name in inference:
		queryEndpoint(curendpointrdfs,content,resultFolder,name)
	else:
		queryEndpoint(curendpoint,content,resultFolder,name)
	f.close()
#print(json.dumps(resultMap, indent=2))
#print(json.dumps(resultMapReq, indent=2))
f = open("benchmarkresult.json", "w")
f.write(json.dumps(resultMap, indent=2))
f.close()
comparefile.close()
calculateComplianceScore(resultMap)
benchmarkResultsToRDF(resultMap)
