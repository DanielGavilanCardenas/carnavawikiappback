# ETAPA DE COMPILACION
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# ETAPA 2: Ejecución
FROM eclipse-temurin:17-jre
# Definimos el puerto
ENV PORT=8083
EXPOSE 8083
# Copiamos el jar usando el nombre exacto de tu pom.xml
COPY --from=build /target/carnavawikiappback-0.1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
