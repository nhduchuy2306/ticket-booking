FROM eclipse-temurin:21-jre-alpine
ENV SPRING_PROFILES_ACTIVE=prod
COPY target/event-service*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]