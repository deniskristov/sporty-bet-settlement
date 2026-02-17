FROM maven:3.9.6-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copy parent POM first
COPY pom.xml ./pom.xml

# Copy module POM
COPY betting-settlement-service//pom.xml ./betting-settlement-service//pom.xml

# Download dependencies
WORKDIR /app/betting-settlement-service/
RUN mvn dependency:go-offline -B

# Copy source and build
COPY betting-settlement-service//src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/betting-settlement-service//target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
