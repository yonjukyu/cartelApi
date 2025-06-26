# Dockerfile simple pour démonstration Render
FROM openjdk:17-jdk-slim

# Install curl pour health check
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy tout le projet
COPY . .

# Make gradlew executable
RUN chmod +x gradlew

# Build l'application
RUN ./gradlew bootJar --no-daemon

# Port dynamique pour Render
EXPOSE ${PORT:-8080}

# Variables d'environnement
ENV SPRING_PROFILES_ACTIVE=demo

# Health check simple
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:${PORT:-8080}/actuator/health || exit 1

# Démarrage avec port dynamique
CMD java -Dserver.port=${PORT:-8080} -jar build/libs/*.jar