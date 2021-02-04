from xml.sax.handler import ContentHandler
from xml.sax import make_parser
import glob
import os, os.path, sys
import pdb
import fileinput

path = os.getcwd()

for filenames in glob.glob(os.path.join("src/main/resources/gsb_answers/", '*.srx')):
    try:
        parser = make_parser()
        parser.setContentHandler(ContentHandler())
        parser.parse(filenames)
        #print ("%s is well-formed" % filenames)
    except Exception:
        print ("%s is NOT well-formed!" % (filenames))