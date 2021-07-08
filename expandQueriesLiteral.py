import os
import itertools


def replaceInString(toreplace, firstliteral, secondliteral,firstliteralrel,secondliteralrel):
	return toreplace.replace("%%literal1%%",firstliteral).replace("%%literal2%%",secondliteral).replace("%%literalrel1%%",firstliteralrel).replace("%%literalrel2%%",secondliteralrel)

querypath = 'src/main/resources/gsb_querytemplates/'

answerpath = 'src/main/resources/gsb_answertemplates/'

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

combinations=list(itertools.combinations(geom_literals,2))
combinations+=list(map(lambda x, y:(x,y), geom_literals.keys(), geom_literals.keys()))

files = os.listdir(path)
first=True
for f in files:
	file = open(querypath+f, "r")
	filecontent=file.read()
	variantcounter=1
	if "%%literal2%%" in filecontent:
		for lit in combinations:
			#print(lit[0]+" "+lit[1]+" "+geom_literals[lit[0]]+" "+geom_literals[lit[1]])
			newfile=replaceInString(filecontent,lit[0],lit[1],geom_literals[lit[0]],geom_literals[lit[1]])
			with open(querypath+"result/"+f[0:f.rfind("-")]+"-"+str(variantcounter)+".rq", "w") as f2:
				f2.write(newfile)
			variantcounter=variantcounter+1
		file.close()
	else:	
		for lit in geom_literals2:
			print(lit[0]+" "+lit[1])
			newfile=replaceInString(filecontent,lit[0],lit[0],lit[1],lit[1])
			with open(querypath+"result/"+f[0:f.rfind("-")]+"-"+str(variantcounter)+".rq", "w") as f2:
				f2.write(newfile)
			variantcounter=variantcounter+1
		file.close()

for f in files:
	file = open(answerpath+f, "r")
	filecontent=file.read()
	variantcounter=1
	if "%%literal2%%" in filecontent:
		for lit in combinations:
			#print(lit[0]+" "+lit[1]+" "+geom_literals[lit[0]]+" "+geom_literals[lit[1]])
			newfile=replaceInString(filecontent,lit[0],lit[1],geom_literals[lit[0]],geom_literals[lit[1]])
			with open(answerpath+"result/"+f[0:f.rfind("-")]+"-"+str(variantcounter)+".rq", "w") as f2:
				f2.write(newfile)
			variantcounter=variantcounter+1
		file.close()
	else:	
		for lit in geom_literals2:
			print(lit[0]+" "+lit[1])
			newfile=replaceInString(filecontent,lit[0],lit[0],lit[1],lit[1])
			with open(answerpath+"result/"+f[0:f.rfind("-")]+"-"+str(variantcounter)+".rq", "w") as f2:
				f2.write(newfile)
			variantcounter=variantcounter+1
		file.close()

	