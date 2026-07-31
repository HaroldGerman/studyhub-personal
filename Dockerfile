# Stage 1: Build React Frontend
FROM node:20-alpine AS frontend-builder
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

# Stage 2: Build Spring Boot Backend
FROM maven:3.9-eclipse-temurin-21-alpine AS backend-builder
WORKDIR /app
# Copy the compiled static frontend resources from the previous stage
COPY --from=frontend-builder /app/backend/src/main/resources/static /app/backend/src/main/resources/static
# Copy backend source
COPY backend/pom.xml ./backend/
COPY backend/src ./backend/src
WORKDIR /app/backend
RUN mvn clean package -DskipTests

# Stage 3: Run Spring Boot App
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=backend-builder /app/backend/target/studyhub-api-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
