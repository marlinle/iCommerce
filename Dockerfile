FROM openjdk:8-jre-alpine

COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","-Ddb.host=host.docker.internal","/app.jar"]

EXPOSE 8080