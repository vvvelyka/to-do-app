# Java-enabled Alpine Linux image
FROM openjdk:21-jdk-slim
EXPOSE 8080
ARG JAR_FILE=target/to-do-app-0.0.1-SNAPSHOT.jar
# Copy jar file to the image
COPY ${JAR_FILE} to-do.jar

ENTRYPOINT ["java","-jar","/to-do.jar"]