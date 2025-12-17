# 🎭 CarnavaWikiApp Back

**CarnavaWikiApp Back** es el backend del proyecto **CarnavaWiki**, una aplicación para gestionar información relacionada con agrupaciones, concursos, ediciones y otros elementos del carnaval.  
El backend está construido con **Spring Boot 3.3.1** y **Java 17**, siguiendo una arquitectura limpia y modular.

---

## 🚀 Tecnologías principales

- **Java 17**
- **Spring Boot 3.3.1**
  - Spring Web
  - Spring Security
  - Spring Data JPA
  - Spring Validation
- **Lombok**
- **Hibernate**
- **JWT (JSON Web Tokens)** para autenticación
- **OpenAPI/Swagger** para documentación de API
- **Maven** para la gestión de dependencias

---

## 🏗️ Estructura del proyecto

```
src/
 ├── main/
 │   ├── java/org/carnavawiky/back/
 │   │   ├── config/         # Configuración general (seguridad, JPA, CORS, Swagger, etc.)
 │   │   ├── controller/     # Controladores REST (Agrupación, Concurso, Usuario, etc.)
 │   │   ├── dto/            # Objetos de transferencia de datos (Request/Response)
 │   │   ├── model/          # Entidades JPA (Agrupacion, Persona, Concurso, etc.)
 │   │   ├── repository/     # Interfaces JPA Repository
 │   │   ├── service/        # Lógica de negocio
 │   │   └── exception/      # Manejo de errores
 │   └── resources/
 │       ├── application.yml # Configuración de entorno
 │       └── static/         # Archivos estáticos (si aplica)
 └── test/                   # Pruebas unitarias e integración
```

---

## ⚙️ Configuración del entorno

### Requisitos previos

- **Java 17+**
- **Maven 3.9+**
- **Base de datos relacional** (ej. PostgreSQL o MySQL)
- **IDE recomendado**: IntelliJ IDEA o VS Code

### Variables de entorno

Configura las siguientes variables (o edita `application.yml`):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/carnavawiki
    username: usuario
    password: contraseña
  jpa:
    hibernate:
      ddl-auto: update
  jwt:
    secret: TU_SECRETO_JWT
    expiration: 86400000
```

---

## ▶️ Ejecución

### Desde línea de comandos

```bash
mvn spring-boot:run
```

### Desde un IDE

Ejecuta la clase principal:

```
org.carnavawiky.back.CarnavawikiappbackApplication
```

La aplicación estará disponible en:

👉 `http://localhost:8080`

---

## 🔑 Autenticación

El proyecto incluye un sistema completo de autenticación mediante JWT:

- Registro de usuarios
- Inicio de sesión
- Renovación de tokens
- Roles y permisos con Spring Security

---

## 🧭 Documentación de API

Swagger/OpenAPI está habilitado para visualizar y probar los endpoints.

Una vez ejecutada la app, accede a:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🧪 Pruebas

Ejecuta los tests con:

```bash
mvn test
```

---

## 🛠️ Compilación del paquete

Para generar el artefacto `.jar` ejecuta:

```bash
mvn clean package
```

El archivo resultante se ubicará en:

```
target/carnavawikiappback-0.0.1-SNAPSHOT.jar
```

---

## 📄 Licencia

Proyecto desarrollado para fines educativos y de documentación cultural.  
© 2025 CarnavaWiki – Todos los derechos reservados.
