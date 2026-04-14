FROM eclipse-temurin:21-jre-alpine
COPY target/api-gateway*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]