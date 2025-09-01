# Multi-stage Dockerfile for User Service
FROM openjdk:21-jdk-slim AS builder

# Set working directory
WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY ../pom.xml ../pom.xml

# Download dependencies
RUN apt-get update && apt-get install -y maven
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:21-jre-slim

# Create non-root user
RUN groupadd -r enterpriseshop && useradd -r -g enterpriseshop enterpriseshop

# Set working directory
WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Change ownership
RUN chown -R enterpriseshop:enterpriseshop /app

# Switch to non-root user
USER enterpriseshop

# Expose port
EXPOSE 8082

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8082/users/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
