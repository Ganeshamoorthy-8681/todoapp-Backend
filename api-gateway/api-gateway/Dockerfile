# Step 1: Build the application using Maven
FROM openjdk:21 AS build
#FROM maven:3.9.0-eclipse-temurin-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy only the necessary files for dependency resolution and downloading dependencies first
COPY .mvn .mvn
COPY mvnw ./
COPY pom.xml ./
COPY src ./src
RUN chmod +x mvnw

RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Package the application into a JAR file
# Use the 'prod' profile to build the JAR for production
RUN ./mvnw clean package -DskipTests



# Step 2: Create the runtime image
FROM eclipse-temurin:21-jdk-alpine AS image

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage to the current stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot app listens on
EXPOSE 8080

# Define the entry point to run the JAR file
# Use the environment variable SPRING_PROFILES_ACTIVE to set the active profile
ENTRYPOINT ["java", "-jar", "app.jar"]
