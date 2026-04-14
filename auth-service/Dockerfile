FROM eclipse-temurin:21-jre-alpine
ENV SPRING_PROFILES_ACTIVE=prod
COPY target/auth-service*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]