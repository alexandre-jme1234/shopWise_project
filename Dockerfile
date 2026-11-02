# --- Étape 1 : compilation du backend (Maven) ---
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /usr/src/app
# dépendances d'abord : couche mise en cache tant que pom.xml ne change pas
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN ./mvnw -q dependency:go-offline
COPY src src
# les tests sont exécutés par la CI (job "backend") ; ici on construit seulement le jar
RUN ./mvnw -q package -DskipTests

# --- Étape 2 : image d'exécution (JRE seul, plus légère) ---
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /usr/src/app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
