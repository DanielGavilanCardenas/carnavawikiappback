[!–- ETAPA DE CONSTRUCCIÓN: Usamos Maven para generar el archivo JAR ejecutable -–]
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

[!–- ETAPA DE EJECUCIÓN: Usamos una imagen ligera de JRE para correr la app -–]
FROM openjdk:17-jdk-slim
[!–- EXPOSE: Informamos que la app usa el puerto 8083 definido en tu application.properties -–]
EXPOSE 8083
[!–- Copiamos el JAR generado desde la etapa de construcción -–]
COPY --from=build /target/carnavawikiappback-0.0.5-SNAPSHOT.jar app.jar

[!–- ENTRYPOINT: Comando para iniciar la aplicación -–]
ENTRYPOINT ["java", "-jar", "/app.jar"]