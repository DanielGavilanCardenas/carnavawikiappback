# ETAPA DE COMPILACION
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# ETAPA 2: Ejecución
FROM openjdk:17-jdk-slim
# Definimos el puerto que tienes en application.properties
ENV PORT=8083
EXPOSE 8083
# Copiamos el jar usando el nombre exacto de tu pom.xml
COPY --from=build /target/carnavawikiappback-0.1.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
