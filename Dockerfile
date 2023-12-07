# Use an official Maven image as a build environment
FROM maven:3.8.4-openjdk-11-slim AS build

# Set the working directory to /app
WORKDIR /app

# Copy the entire project to the container
COPY . .

# Set the Java version for Maven
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
RUN export PATH=$JAVA_HOME/bin:$PATH

# Build the application
RUN mvn package -DskipTests

# Use an official OpenJDK 11 runtime as the final image
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
