FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .
COPY src ./src

RUN mvn -DskipTests package

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

RUN useradd --system --uid 1001 appuser

COPY --from=build /workspace/target/parkcontrol-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 3003

USER appuser

ENTRYPOINT ["java", "-jar", "/app/app.jar"]