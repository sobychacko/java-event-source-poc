#!/bin/bash

cp ~/.m2/repository/io/projectriff/java-supplier-invoker/0.0.1-SNAPSHOT/java-supplier-invoker-0.0.1-SNAPSHOT.jar .
cp ~/.m2/repository/io/projectriff/time-supplier/0.0.1-SNAPSHOT/time-supplier-0.0.1-SNAPSHOT.jar .

docker build -t gcr.io/$GCP_PROJECT/time-event-source .
docker push gcr.io/$GCP_PROJECT/time-event-source

rm *.jar
