FROM eclipse-temurin:21-jre-alpine
COPY target/order-service*.jar app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]