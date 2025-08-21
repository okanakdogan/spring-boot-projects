# Use Maven with Java 21
FROM maven:3.9-eclipse-temurin-21

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project (pom.xml, src/, etc.)
COPY . .

# Expose port 8080 (Spring Boot default) and 5005 (debug)
EXPOSE 8080 5005

# Run Spring Boot app with debugging enabled
CMD ["mvn", "spring-boot:run"]
