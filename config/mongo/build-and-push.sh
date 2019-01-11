#!/bin/bash

cp ~/.m2/repository/io/projectriff/java-supplier-invoker/0.0.1-SNAPSHOT/java-supplier-invoker-0.0.1-SNAPSHOT.jar .
cp ~/.m2/repository/io/projectriff/mongo-supplier/0.0.1-SNAPSHOT/mongo-supplier-0.0.1-SNAPSHOT.jar .

docker build -t gcr.io/$GCP_PROJECT/mongo-event-source .
docker push gcr.io/$GCP_PROJECT/mongo-event-source

rm *.jar
