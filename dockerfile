# Build stage
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./gradlew build -x test

# Run stage  
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create app user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy built jar from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Set ownership
RUN chown -R appuser:appgroup /app

# Switch to app user
USER appuser

# Set JVM options
ENV JAVA_OPTS="-Xms512m -Xmx512m"

# Expose port
EXPOSE 8080

# Set entrypoint
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]