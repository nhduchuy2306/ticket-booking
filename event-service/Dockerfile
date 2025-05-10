## Use Mavento build the application
#FROM maven:3.9.8-eclipse-temurin-21 AS build
#LABEL authors="nnhy"
#
## Set the working directory for the build
#WORKDIR /app
#
## Copy the Maven project files
#COPY pom.xml .
#COPY src ./src
#
## Build the application
#RUN mvn clean package
#
## Use the minimal java runtim for the final image
#FROM openjdk:21-jdk-slim
#
## Add a volum to hold the application data
#VOLUME /tmp
#
## The application's jar file
#ARG JAR_FILE=target/api-gateway-1.0-SNAPSHOT.jar
#
## Copy the jar file from the build stage to the final image
#COPY --from=build /app/${JAR_FILE} app.jar
#
## Run the jar file
#ENV SPRING_PROFILES_ACTIVE=prod
#ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "/app.jar"]

FROM eclipse-temurin:21-jre-alpine
COPY target/event-service*.jar app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]