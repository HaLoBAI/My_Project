FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy parent pom and common module
COPY pom.xml .
COPY common ./common

# Copy product-service
COPY product-service ./product-service

# Build
RUN mvn clean package -DskipTests -pl product-service -am

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/product-service/target/product-service-1.0.0.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
