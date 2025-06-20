# Use an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the application JAR (adjust filename if needed)
COPY target/skill-mentor-root-*.jar app.jar

# Expose the app port
EXPOSE 8443

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
