from xml.sax.handler import ContentHandler
from xml.sax import make_parser
import glob
import os, os.path, sys
import pdb
import fileinput
import rdflib
from rdflib.plugins.sparql import prepareQuery

path = os.getcwd()

f = open("validationresult.txt", "w")
for filenames in glob.glob(os.path.join("../src/main/resources/gsb_queries/", '*.rq')):
    try:
        with open(filenames, 'r') as file:
            query = file.read().replace('\n', '')
        prepareQuery(query)
        #print ("%s is well-formed" % filenames)
    except Exception as e:
        print ("%s is NOT well-formed!\nError: %s" % (filenames,e))
        f.write(str(filenames)+" is not well-formed:\nError: "+str(e)+"\n")

for filenames in glob.glob(os.path.join("../src/main/resources/gsb_answers/", '*.srx')):
    try:
        parser = make_parser()
        parser.setContentHandler(ContentHandler())
        parser.parse(filenames)
        #print ("%s is well-formed" % filenames)
    except Exception as e:
        print ("%s is NOT well-formed!\nError: %s" % (filenames,e))
        f.write(str(filenames)+" is not well-formed:\nError: "+str(e)+"\n")		
f.close()
