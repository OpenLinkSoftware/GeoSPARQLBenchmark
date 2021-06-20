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

## Acknowledgement

The benchmark has been developed as part of the [HOBBIT](https://project-hobbit.eu) and [SAGE](https://sage.cs.uni-paderborn.de/sage/) research projects.
