# Use OpenJDK 17 from the official Docker Hub repository
FROM openjdk:17-jdk-slim

# Set a working directory
COPY target/fresher-management-0.0.1-SNAPSHOT.jar /fresher-management-0.0.1-SNAPSHOT.jar

# Specify the command to run on container startup
ENTRYPOINT["java", "jar", "/fresher-management-0.0.1-SNAPSHOT.jar"]