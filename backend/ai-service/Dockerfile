FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy parent pom and common module
COPY pom.xml .
COPY common ./common

# Copy ai-service
COPY ai-service ./ai-service

# Build
RUN mvn clean package -DskipTests -pl ai-service -am

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/ai-service/target/ai-service-1.0.0.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]
