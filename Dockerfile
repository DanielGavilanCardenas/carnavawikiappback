# ETAPA DE COMPILACION
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
#  Forzamos que el JAR se genere con un nombre predecible para evitar problemas de versiones
RUN mvn clean package -DskipTests

# Ejecución
FROM eclipse-temurin:17-jre
# Definimos el puerto y el perfil por defecto
ENV PORT=8083
ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8083

# Usamos el asterisco para copiar cualquier JAR generado en target.
# Como el 'mvn clean' asegura que solo haya uno, esto siempre funcionará.
COPY --from=build /target/*.jar app.jar

# Spring Boot reconoce automáticamente la variable de entorno SPRING_PROFILES_ACTIVE
ENTRYPOINT ["java", "-jar", "/app.jar"]