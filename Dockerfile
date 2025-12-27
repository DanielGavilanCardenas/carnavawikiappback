# ETAPA DE COMPILACION
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# ETAPA DE EJECUCION
FROM openjdk:17-jdk-slim
# Definimos la variable de entorno para el puerto 8083
ENV PORT=8083
EXPOSE 8083

# Copiamos el archivo JAR generado
# Nota: El nombre debe coincidir con el artifactId y version del pom.xml
COPY --from=build /target/carnavawikiappback-0.0.7-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
