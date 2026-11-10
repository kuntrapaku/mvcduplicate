FROM eclipse-temurin:17-jre
# Start with an image containing the Java 17 runtime.

WORKDIR /app
# Set the working folder inside the image.

COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
# Copy your existing application JAR into /app/app.jar.

EXPOSE 8080
# Document the application's port; this does not publish it.

ENTRYPOINT ["java", "-jar", "app.jar"]
# Start the application when a container runs.