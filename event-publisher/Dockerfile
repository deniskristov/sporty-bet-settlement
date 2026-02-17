FROM maven:3.9.6-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copy parent POM first
COPY pom.xml ./pom.xml

# Copy module POM
COPY event-publisher/pom.xml ./event-publisher/pom.xml

# Download dependencies
WORKDIR /app/event-publisher
RUN mvn dependency:go-offline -B

# Copy source and build
COPY event-publisher/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/event-publisher/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
