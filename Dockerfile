# Use an official OpenJDK base image
FROM openjdk:17-jdk-slim

# Add metadata
LABEL maintainer="chethiya.wanigarathne@gmail.com"
LABEL version="1.0"
LABEL description="Docker image for Skill Mentor Spring Boot App"

# Set the working directory
WORKDIR /app

# Copy the jar file built by Maven
COPY target/skill-mentor-root-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on (Railway expects 8080)
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
