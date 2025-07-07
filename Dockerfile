# Start with an OpenJDK image
FROM openjdk:17-jdk-slim

# Add a volume and set working dir
VOLUME /tmp
WORKDIR /app

# Copy build output to container
COPY target/app-0.0.1-SNAPSHOT.jar app.jar

# Run the app
ENTRYPOINT ["java","-jar","app.jar"]
