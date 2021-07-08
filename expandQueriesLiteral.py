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

combinations=list(itertools.permutations(geom_literals,2))
combinations+=list(map(lambda x, y:(x,y), geom_literals.keys(), geom_literals.keys()))

for comb in combinations:
    print(comb)

files = os.listdir(querypath)
answerp = os.listdir(answerpath)
first=True
for f in files:
	if not os.path.isfile(querypath+f):
		continue
	file = open(querypath+f, "r")
	filecontent=file.read()
	fileprefix=f[0:f.rfind("-")]
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
		file.close()
		continue
	if "%%literal2%%" in filecontent:
		for lit in combinations:
			#print(lit[0]+" "+lit[1]+" "+geom_literals[lit[0]]+" "+geom_literals[lit[1]])
			newfile=replaceInString(filecontent,lit[0],lit[1],geom_literals[lit[0]],geom_literals[lit[1]])
			with open(querypath+"result/"+f[0:f.rfind("-")]+"-"+str(variantcounter)+".rq", "w") as f2:
				f2.write(newfile)
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
			variantcounter=variantcounter+1
		file.close()

"""
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
"""
	
