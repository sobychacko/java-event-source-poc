FROM java:8-alpine
VOLUME /tmp

COPY java-supplier-invoker-0.0.1-SNAPSHOT.jar app.jar
COPY mongo-supplier-0.0.1-SNAPSHOT.jar supplier.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--function.uri=file:///supplier.jar"]
