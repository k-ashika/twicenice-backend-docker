# Use Java 17 base image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory in container
WORKDIR /app

# Copy everything from your project folder to /app
COPY . .

# Run Maven wrapper to package your Spring Boot app, skipping tests
RUN ./mvnw clean package -DskipTests

# Expose port 8080 (Spring Boot default)
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "target/twicenice-backend-0.0.1-SNAPSHOT.jar"]
