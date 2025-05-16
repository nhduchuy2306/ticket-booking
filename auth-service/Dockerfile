FROM eclipse-temurin:21-jre-alpine
COPY target/auth-service*.jar app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]