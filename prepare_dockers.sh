#!/bin/bash

mvn package

docker build -f docker/gsb-benchmarkcontroller.docker -t git.project-hobbit.eu:4567/mjovanovik/gsb-benchmarkcontroller .
docker build -f docker/gsb-datagenerator.docker -t git.project-hobbit.eu:4567/mjovanovik/gsb-datagenerator .
docker build -f docker/gsb-seqtaskgenerator.docker -t git.project-hobbit.eu:4567/mjovanovik/gsb-seqtaskgenerator .
docker build -f docker/gsb-evaluationmodule.docker -t git.project-hobbit.eu:4567/mjovanovik/gsb-evaluationmodule .

docker push git.project-hobbit.eu:4567/mjovanovik/gsb-benchmarkcontroller
docker push git.project-hobbit.eu:4567/mjovanovik/gsb-datagenerator
docker push git.project-hobbit.eu:4567/mjovanovik/gsb-seqtaskgenerator
docker push git.project-hobbit.eu:4567/mjovanovik/gsb-evaluationmodule
