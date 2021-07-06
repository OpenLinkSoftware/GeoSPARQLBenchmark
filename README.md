# GeoSPARQL Compliance Benchmark

This is the GeoSPARQL Compliance Benchmark, integrated into the [HOBBIT Platform](https://github.com/hobbit-project/platform).

The GeoSPARQL Compliance Benchmark aims to evaluate the GeoSPARQL compliance of RDF storage systems. The benchmark uses
206 SPARQL queries to test the extent to which the benchmarked system supports the 30 requirements defined in the [GeoSPARQL standard](https://www.ogc.org/standards/geosparql).

As a result, the benchmark provides two metrics:
 * **Correct answers**: The number of correct answers out of all GeoSPARQL queries, i.e. tests.
 * **GeoSPARQL compliance percentage**: The percentage of compliance with the requirements of the GeoSPARQL standard.

## Results

You can find a set of results from the [latest experiments on the hosted instance of the HOBBIT Platform](https://master.project-hobbit.eu/experiments/1612476122572,1612477003063,1612476116049,1612477500164,1612661614510,1612637531673,1612828110551,1612477849872)
(log in as Guest). [last update: 09.02.2021]

If you want your RDF triplestore tested, you can [add it as a system to the HOBBIT Platform](https://hobbit-project.github.io/system_integration.html),
and then [run an experiment](https://hobbit-project.github.io/benchmarking.html) using the [hosted instance of the HOBBIT Platform](https://hobbit-project.github.io/master.html).

## Publications

 * Milos Jovanovik, Timo Homburg, Mirko Spasić. "[Software for the GeoSPARQL Compliance Benchmark](https://doi.org/10.1016/j.simpa.2021.100071)". Software Impacts 8:100071, 2021.
 * (preprint) Milos Jovanovik, Timo Homburg, Mirko Spasić. "[A GeoSPARQL Compliance Benchmark](https://arxiv.org/abs/2102.06139)". arXiv:2102.06139.

## Mapping Requirements to Queries

Req. | Set of corresponding queries | Description
:---: | :--- | :--- 
R1 | [Q01.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r01.rq) | Selection of the first triple where geometry A is the subject
R2 | [Q02.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r02.rq) | Selection of the first entity of type geo:SpatialObject
R3 | [Q03.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r03.rq) | Selection of the first entity of type geo:Feature
R4 | [Q04-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r04-1.rq), [Q04-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r04-2.rq),  [Q04-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r04-3.rq), [Q04-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r04-4.rq), <br /> [Q04-5.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r04-5.rq), [Q04-6.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r04-6.rq),  [Q04-7.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r04-7.rq), [Q04-8.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r04-8.rq) | Testing the properties geo:sfEquals, geo:sfDisjoint, geo:sfIntersects,  geo:sfTouches, geo:sfCrosses, geo:sfWithin, geo:sfContains and  geo:sfOverlaps
R5 | [Q05-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r05-1.rq), [Q05-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r05-2.rq),  [Q05-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r05-3.rq), [Q05-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r05-4.rq), <br /> [Q05-5.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r05-5.rq), [Q05-6.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r05-6.rq),  [Q05-7.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r05-7.rq), [Q05-8.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r05-8.rq) | Testing the properties geo:ehEquals, geo:ehDisjoint, geo:ehMeet,  geo:ehOverlap, geo:ehCovers, geo:ehCoveredBy, geo:ehInside and  geo:ehContains
R6 | [Q06-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r06-1.rq), [Q06-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r06-2.rq),  [Q06-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r06-3.rq), [Q06-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r06-4.rq), <br /> [Q06-5.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r06-5.rq), [Q06-6.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r06-6.rq),  [Q06-7.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r06-7.rq), [Q06-8.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r06-8.rq) | Testing the properties geo:rcc8eq, geo:rcc8dc, geo:rcc8ec, geo:rcc8po,  geo:rcc8tppi, geo:rcc8tpp, geo:rcc8ntpp and geo:rcc8ntppi
R7 | [Q07.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r07.rq) | Selection of all entities of type geo:Geometry
R8 | [Q08-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r08-1.rq), [Q08-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r08-2.rq) | Selection of the value of geometry A denoted by the properties  geo:hasGeometry and geo:hasDefaultGeometry
R9 | [Q09-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r09-1.rq), [Q09-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r09-2.rq),  [Q09-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r09-3.rq), [Q09-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r09-4.rq), <br /> [Q09-5.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r09-5.rq), [Q09-6.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r09-6.rq) | Selection of the value of geometry A denoted by the properties  geo:dimension, geo:coordinateDimension, geo:spatialDimension,  geo:isEmpty, geo:isSimple and geo:hasSerialization
R10 | [Q10.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r10.rq) | Check of the datatype of a correctly defined WKT literal from the dataset
R11 | [Q11.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r11.rq) | Equality check of two geometries from the dataset
R12 | [Q12.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r12.rq) | Check if the system interprets the axis order within a point geometry  according to the spatial reference system being used
R13 | [Q13-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r13-1.rq), [Q13-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r13-2.rq) | Check if an empty RDFS Literal of type geo:wktLiteral is interpreted as  an empty geometry
R14 | [Q14.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r14.rq) | Check of the geo:asWKT value of geometry A against the expected  literal value
R15 | [Q15.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r15.rq) | Check whether all the values of the geo:asGML property contain a valid  GM_Object subtype in it and whether its datatype is geo:gmlLiteral
R16 | [Q16-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r16-1.rq), [Q16-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r16-2.rq) | Check if an empty geo:gmlLiteral is interpreted as an empty geometry
R17 | --- | ---
R18 | [Q18.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r18.rq) | Checking the geo:asGML value of geometry A against the expected literal value
R19 | [Q19-1-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-1-1.rq), [Q19-1-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-1-2.rq),  [Q19-1-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-1-3.rq), [Q19-1-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-1-4.rq), <br /> [Q19-2-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-2-1.rq), [Q19-2-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-2-2.rq),  [Q19-3-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-3-1.rq), [Q19-3-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-3-2.rq), <br /> [Q19-4-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-4-1.rq), [Q19-4-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-4-2.rq),  [Q19-4-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-4-3.rq), [Q19-4-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-4-4.rq), <br /> [Q19-5-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-5-1.rq), [Q19-5-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-5-2.rq),  [Q19-5-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-5-3.rq), [Q19-5-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-5-4.rq), <br /> [Q19-6-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-6-1.rq), [Q19-6-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-6-2.rq),  [Q19-6-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-6-3.rq), [Q19-6-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-6-4.rq), <br /> [Q19-7-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-7-1.rq), [Q19-7-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-7-2.rq),  [Q19-7-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-7-3.rq), [Q19-7-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-7-4.rq), <br /> [Q19-8-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-8-1.rq), [Q19-8-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-8-2.rq),  [Q19-9-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-9-1.rq), [Q19-9-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r19-9-2.rq) | Checking a support of the geospatial functions geof:distance, geof:buffer,  geof:convexHull, geof:intersection, geof:union, geof:difference,  geof:symDifference, geof:envelope and geof:boundary
R20 | [Q20-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r20-1.rq), [Q20-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r20-2.rq) | Checking a support of the geospatial function geof:getSRID
R21 | [Q21-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r21-1.rq), [Q21-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r21-2.rq),  [Q21-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r21-3.rq), [Q21-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r21-4.rq) | Checking a support of the geospatial operator geof:relate
R22 | [Q22-1-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-1-1.rq), [Q22-1-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-1-2.rq),  [Q22-1-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-1-3.rq), [Q22-1-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-1-4.rq), <br /> [Q22-2-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-2-1.rq), [Q22-2-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-2-2.rq),  [Q22-2-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-2-3.rq), [Q22-2-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-2-4.rq), <br /> [Q22-3-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-3-1.rq), [Q22-3-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-3-2.rq),  [Q22-3-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-3-3.rq), [Q22-3-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-3-4.rq), <br /> [Q22-4-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-4-1.rq), [Q22-4-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-4-2.rq),  [Q22-4-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-4-3.rq), [Q22-4-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-4-4.rq), <br /> [Q22-5-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-5-1.rq), [Q22-5-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-5-2.rq),  [Q22-5-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-5-3.rq), [Q22-5-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-5-4.rq), <br /> [Q22-6-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-6-1.rq), [Q22-6-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-6-2.rq),  [Q22-6-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-6-3.rq), [Q22-6-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-6-4.rq), <br /> [Q22-7-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-7-1.rq), [Q22-7-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-7-2.rq),  [Q22-7-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-7-3.rq), [Q22-7-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-7-4.rq), <br /> [Q22-8-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-8-1.rq), [Q22-8-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-8-2.rq),  [Q22-8-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-8-3.rq), [Q22-8-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r22-8-4.rq) | Checking a support of the geospatial functions geof:sfEquals, geof:sfDisjoint,  geof:sfIntersects, geof:sfTouches, geof:sfCrosses, geof:sfWithin, geof:sfContains  and geof:sfOverlaps
R23 | [Q23-1-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-1-1.rq), [Q23-1-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-1-2.rq),  [Q23-1-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-1-3.rq), [Q23-1-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-1-4.rq), <br /> [Q23-2-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-2-1.rq), [Q23-2-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-2-2.rq),  [Q23-2-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-2-3.rq), [Q23-2-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-2-4.rq), <br /> [Q23-3-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-3-1.rq), [Q23-3-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-3-2.rq),  [Q23-3-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-3-3.rq), [Q23-3-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-3-4.rq), <br /> [Q23-4-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-4-1.rq), [Q23-4-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-4-2.rq),  [Q23-4-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-4-3.rq), [Q23-4-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-4-4.rq), <br /> [Q23-5-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-5-1.rq), [Q23-5-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-5-2.rq),  [Q23-5-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-5-3.rq), [Q23-5-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-5-4.rq), <br /> [Q23-6-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-6-1.rq), [Q23-6-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-6-2.rq),  [Q23-6-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-6-3.rq), [Q23-6-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-6-4.rq), <br /> [Q23-7-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-7-1.rq), [Q23-7-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-7-2.rq),  [Q23-7-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-7-3.rq), [Q23-7-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-7-4.rq), <br /> [Q23-8-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-8-1.rq), [Q23-8-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-8-2.rq),  [Q23-8-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-8-3.rq), [Q23-8-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r23-8-4.rq) | Checking a support of the geospatial functions geof:ehEquals, geof:ehDisjoint,  geof:ehMeet, geof:ehOverlap, geof:ehCovers, geof:ehCoveredBy, geof:ehInside  and geof:ehContains
R24 | [Q24-1-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-1-1.rq), [Q24-1-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-1-2.rq),  [Q24-1-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-1-3.rq), [Q24-1-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-1-4.rq), <br /> [Q24-2-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-2-1.rq), [Q24-2-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-2-2.rq),  [Q24-2-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-2-3.rq), [Q24-2-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-2-4.rq), <br /> [Q24-3-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-3-1.rq), [Q24-3-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-3-2.rq),  [Q24-3-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-3-3.rq), [Q24-3-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-3-4.rq), <br /> [Q24-4-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-4-1.rq), [Q24-4-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-4-2.rq),  [Q24-4-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-4-3.rq), [Q24-4-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-4-4.rq), <br /> [Q24-5-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-5-1.rq), [Q24-5-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-5-2.rq),  [Q24-5-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-5-3.rq), [Q24-5-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-5-4.rq), <br /> [Q24-6-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-6-1.rq), [Q24-6-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-6-2.rq),  [Q24-6-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-6-3.rq), [Q24-6-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-6-4.rq), <br /> [Q24-7-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-7-1.rq), [Q24-7-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-7-2.rq),  [Q24-7-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-7-3.rq), [Q24-7-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-7-4.rq), <br /> [Q24-8-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-8-1.rq), [Q24-8-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-8-2.rq),  [Q24-8-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-8-3.rq), [Q24-8-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r24-8-4.rq) | Checking a support of the geospatial functions geof:rcc8eq, geof:rcc8dc, geof:rcc8ec,  geof:rcc8po, geof:rcc8tppi, geof:rcc8tpp, geof:rcc8ntpp and geof:rcc8ntppi
R25 | [Q25-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r25-1.rq), [Q25-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r25-2.rq),  [Q25-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r25-3.rq) | Checking if the system supports selecting both materialized RDF triples, as well as  inferred RDF triples based on the RDFS Entailment Regime
R26 | [Q26-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r26-1.rq), [Q26-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r26-2.rq) | Checking if the system supports selecting both materialized RDF triples, as well as  inferred RDF triples based on the RDFS/OWL class hierarchy of geometry types from  Simple Features
R27 | [Q27.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r27.rq) | Checking if the system supports selecting both materialized RDF triples, as well as  inferred RDF triples based on the RDFS/OWL class hierarchy of geometry types of  the GML schema 
R28 | [Q28-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r28-1.rq), [Q28-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r28-2.rq),  [Q28-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r28-3.rq), [Q28-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r28-4.rq), <br /> [Q28-5.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r28-5.rq), [Q28-6.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r28-6.rq),  [Q28-7.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r28-7.rq), [Q28-8.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r28-8.rq) | Testing the properties geo:sfEquals, geo:sfDisjoint, geo:sfIntersects, geo:sfTouches,  geo:sfCrosses, geo:sfWithin, geo:sfContains and geo:sfOverlaps using both  materialized RDF triples and inferred RDF triples 
R29 | [Q29-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r29-1.rq), [Q29-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r29-2.rq),  [Q29-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r29-3.rq), [Q29-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r29-4.rq), <br /> [Q29-5.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r29-5.rq), [Q29-6.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r29-6.rq),  [Q29-7.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r29-7.rq), [Q29-8.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r29-8.rq) | Testing the properties geo:ehEquals, geo:ehDisjoint, geo:ehMeet, geo:ehOverlap,  geo:ehCovers, geo:ehCoveredBy, geo:ehInside and geo:ehContains using both  materialized RDF triples and inferred RDF triples 
R30 | [Q30-1.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r30-1.rq), [Q30-2.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r30-2.rq),  [Q30-3.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r30-3.rq), [Q30-4.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r30-4.rq), <br /> [Q30-5.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r30-5.rq), [Q30-6.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r30-6.rq),  [Q30-7.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r30-7.rq), [Q30-8.rq](https://github.com/OpenLinkSoftware/GeoSPARQLBenchmark/blob/master/src/main/resources/gsb_queries/query-r30-8.rq) | Testing the properties geo:rcc8eq, geo:rcc8dc, geo:rcc8ec, geo:rcc8po, geo:rcc8tppi,  geo:rcc8tpp, geo:rcc8ntpp and geo:rcc8ntppi using both materialized RDF triples  and inferred RDF triples
 

## Acknowledgement

The benchmark has been developed as part of the [HOBBIT](https://project-hobbit.eu) and [SAGE](https://sage.cs.uni-paderborn.de/sage/) research projects.
