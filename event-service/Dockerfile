FROM eclipse-temurin:21-jre-alpine
COPY target/event-service*.jar app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]