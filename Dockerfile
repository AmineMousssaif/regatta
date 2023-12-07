# Use an official Maven image as a build environment
FROM ubuntu:latest AS build
RUN apt-get-update
RUN apt-get install openjdk-11-jdk -y

# Set the working directory to /app
WORKDIR /app

# Copy the entire project to the container
COPY . .

# Build the application
RUN mvn package -DskipTests

# Use an official OpenJDK runtime as the final image
FROM openjdk:11-jre-slim

# Set the working directory to /app
WORKDIR /app

# Copy the JAR file from the build stage to the final image
COPY --from=build /app/target/your-spring-boot-app.jar /app/your-spring-boot-app.jar

# Expose the port that your application will run on
EXPOSE 8080

# Define environment variable
ENV JAVA_OPTS=""

# Run your application when the container starts
CMD ["sh", "-c", "java $JAVA_OPTS -jar your-spring-boot-app.jar"]
# Copy the POM file first for efficient dependency caching
COPY pom.xml .

# Download the dependencies and build the application
RUN mvn dependency:go-offline
RUN mvn package -DskipTests

# Use an official OpenJDK runtime as the final image
FROM openjdk:20-jre-slim

# Set the working directory to /app
WORKDIR /app

# Copy the JAR file from the build stage to the final image
COPY --from=build /app/target/your-spring-boot-app.jar /app/your-spring-boot-app.jar

# Expose the port that your application will run on
EXPOSE 8080

# Define environment variable
ENV JAVA_OPTS=""

# Run your application when the container starts
CMD ["sh", "-c", "java $JAVA_OPTS -jar your-spring-boot-app.jar"]
