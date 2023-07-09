FROM eclipse-temurin:17-jdk-focal
LABEL authors="lamarck"

WORKDIR /app

COPY .mvn ./.mvn

COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline

COPY src ./src

# Entrypoint
CMD ["./mvnw", "spring-boot:run"]