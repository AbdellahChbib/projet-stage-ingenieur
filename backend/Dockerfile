# Étape 1 : Builder le projet
FROM maven:3.8.7-eclipse-temurin-17 AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : Image finale minimaliste pour exécution
FROM eclipse-temurin:17-jdk

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
