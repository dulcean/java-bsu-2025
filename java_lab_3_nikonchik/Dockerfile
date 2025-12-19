FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests \
    -Dspring-boot.repackage.mainClass=backend.Application

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=builder /app/target/anime-random-app-1.0.0.jar app.jar

RUN mkdir -p /data

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]