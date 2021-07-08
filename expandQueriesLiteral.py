import os
import itertools


def replaceInString(toreplace, firstliteral, secondliteral):
	res=""
	asWKTCounter=0
	for line in toreplace.split("\n"):
		if not "geo:asWKT" in line:
			res+=line+"\n"
		elif asWKTCounter==0:
			res+=line.replace("asWKT","as"+firstliteral)+"\n"
			asWKTCounter=asWKTCounter+1
		else:
			res+=line.replace("asWKT","as"+secondliteral)+"\n"
	return res

path = 'src/main/resources/gsb_querytemplates/'

geom_literals = [
    "WKT",
	"GML",
    "GeoJSON",
    "KML",
	"DGGS"
]

combinations=list(itertools.combinations(geom_literals,2))
combinations+=list(map(lambda x, y:(x,y), geom_literals, geom_literals))

files = os.listdir(path)
first=True
for f in files:
	file = open(path+f, "r")
	filecontent=file.read()
	variantcounter=1
	for lit in combinations:
		if first:
			print(lit)
		newfile=replaceInString(filecontent,lit[0],lit[1])
		with open(path+"result/"+f[0:f.rfind("-")]+"-"+str(variantcounter)+".rq", "w") as f2:
			f2.write(newfile)
		variantcounter=variantcounter+1
	first=False
	file.close()
	