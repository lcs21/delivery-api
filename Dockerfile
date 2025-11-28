
# ===== STAGE 1: build =====
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -q -e -DskipTests clean package

# ===== STAGE 2: runtime =====
FROM eclipse-temurin:21-jre-alpine

ENV APP_NAME=delivery-api \
    SERVER_PORT=8080 \
    SPRING_PROFILES_ACTIVE=default \
    JAVA_OPTS=""

RUN addgroup -S app && adduser -S app -G app
USER app

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
HEALTHCHECK --interval=10s --timeout=5s --start-period=30s CMD wget -qO- http://localhost:8080/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar --server.port=${SERVER_PORT} --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
