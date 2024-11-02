# Use a Java runtime image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle build files
COPY build.gradle settings.gradle /app/

COPY gradlew /app/gradlew
COPY gradle /app/gradle

# Copy the application code
COPY src /app/src

RUN chmod +x gradlew

# Build the application
RUN ./gradlew bootJar


# Expose the application port
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "build/libs/prestabanco-0.0.1-SNAPSHOT.jar"]
