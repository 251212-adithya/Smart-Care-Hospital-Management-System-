# Stage 1: Build the application using Maven
FROM maven:3.9.8-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Package the application skipping tests to speed up the build
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image using a lightweight Java 17 JRE
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy the built jar from the builder stage
COPY --from=builder /app/target/smartcare-hms-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
