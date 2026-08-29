FROM eclipse-temurin:21-jre-alpine
LABEL maintainer="GYP Ticket Booking"

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 9006

ENTRYPOINT ["java", "-jar", "app.jar"]
