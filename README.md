# 🎭 CarnavaWikiApp Back

**CarnavaWikiApp Back** es el backend del proyecto **CarnavaWiki**, una plataforma integral para la gestión y consulta de información sobre el Carnaval. Permite administrar agrupaciones, concursos, ediciones, premios, componentes, y contenido multimedia (imágenes y vídeos).

El backend está construido con **Spring Boot 3.3.1** y **Java 17**, siguiendo una arquitectura limpia y modular, y utiliza **Cloudinary** para la gestión de imágenes.

----

## 🚀 Tecnologías principales

- **Java 17**
- **Spring Boot 3.3.1**
  - **Spring Web**: Para la creación de la API REST.
  - **Spring Security**: Gestión de autenticación y autorización basada en roles.
  - **Spring Data JPA**: Persistencia de datos con Hibernate.
  - **Spring Validation**: Validación de datos de entrada.
- **Base de Datos**: MySQL (producción) / H2 (tests).
- **Cloudinary**: Almacenamiento y gestión de imágenes en la nube.
- **Lombok**: Reducción de código repetitivo (boilerplate).
- **JWT (JSON Web Tokens)**: Autenticación segura y sin estado (Stateless).
- **OpenAPI/Swagger**: Documentación interactiva de la API.
- **Maven**: Gestión de dependencias y ciclo de vida del proyecto.
- **Docker**: Contenerización de la aplicación (Dockerfile incluido).

---

## 🏗️ Estructura del proyecto

El proyecto sigue una arquitectura en capas clásica de Spring Boot:

```
src/
 ├── main/
 │   ├── java/org/carnavawiky/back/
 │   │   ├── config/         # Configuración (Security, Cloudinary, Swagger, CORS, JPA Auditing)
 │   │   ├── controller/     # Controladores REST (Endpoints de la API)
 │   │   ├── dto/            # Data Transfer Objects (Request/Response)
 │   │   ├── model/          # Entidades JPA (Base de datos)
 │   │   ├── repository/     # Interfaces de acceso a datos (Spring Data JPA)
 │   │   ├── service/        # Lógica de negocio
 │   │   ├── security/       # Filtros JWT y utilidades de seguridad
 │   │   ├── mapper/         # Mapeadores entre Entidades y DTOs
 │   │   └── exception/      # Manejo global de excepciones
 │   └── resources/
 │       ├── application.yml # Configuración principal
 │       └── static/         # Recursos estáticos
 └── test/                   # Pruebas unitarias e integración (JUnit 5, Mockito)
```

### Módulos Principales (Modelos)
- **Usuarios y Roles**: Gestión de usuarios, roles (ADMIN, ESPECIALISTO, USER) y autenticación.
- **Agrupaciones**: Gestión de agrupaciones de carnaval (Chirigotas, Comparsas, Coros, Cuartetos).
- **Concursos y Ediciones**: Información sobre concursos (COAC, etc.) y sus ediciones anuales.
- **Componentes y Personas**: Registro de autores, directores y componentes de las agrupaciones.
- **Multimedia**:
  - **Imágenes**: Subida y gestión a través de Cloudinary.
  - **Vídeos**: Enlaces a YouTube verificados por especialistas.
- **Premios y Localidades**: Gestión de palmarés y ubicaciones geográficas.

---

## ⚙️ Configuración del entorno

### Requisitos previos

- **Java 17+**
- **Maven 3.9+**
- **MySQL** (o base de datos compatible)
- **Cuenta en Cloudinary** (para gestión de imágenes)

### Variables de entorno

Es necesario configurar las siguientes variables en tu entorno o en el archivo `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/carnavawiki?useSSL=false&serverTimezone=UTC
    username: TU_USUARIO_DB
    password: TU_PASSWORD_DB
  jpa:
    hibernate:
      ddl-auto: update # 'update' para desarrollo, 'validate' para producción
  
  # Configuración JWT
  jwt:
    secret: TU_SECRETO_JWT_MUY_LARGO_Y_SEGURO
    expiration: 86400000 # 24 horas en milisegundos

# Configuración Cloudinary
cloudinary:
  cloud_name: TU_CLOUD_NAME
  api_key: TU_API_KEY
  api_secret: TU_API_SECRET
```

---

## ▶️ Ejecución

### Desde línea de comandos

```bash
mvn spring-boot:run
```

### Desde un IDE

Ejecuta la clase principal:
`org.carnavawiky.back.CarnavawikiappbackApplication`

La API estará disponible en: 👉 `http://localhost:8080`

---

## 🔑 Seguridad y Roles

El sistema implementa seguridad basada en **JWT** y **Roles**:

| Rol | Descripción | Permisos Principales |
| :--- | :--- | :--- |
| **USER** | Usuario registrado básico | Consultar información pública, ver imágenes/vídeos. |
| **ESPECIALISTO** | Usuario colaborador experto | Gestionar vídeos (crear, verificar), acceder a endpoints de especialista. |
| **ADMIN** | Administrador del sistema | Gestión total: Usuarios, Localidades, Imágenes (subir/borrar), Agrupaciones, etc. |

**Endpoints Públicos:**
- `/api/auth/**` (Login, Registro)
- `/api/public/health` (Health check)
- `/api/videos/public` (Listar vídeos verificados)
- `/v3/api-docs/**`, `/swagger-ui/**` (Documentación)

---

## 🧭 Documentación de API

Swagger UI está habilitado para explorar y probar los endpoints interactivamente.

Accede a:
👉 `http://localhost:8080/swagger-ui/index.html`

---

## 🧪 Pruebas

El proyecto cuenta con una amplia cobertura de tests unitarios y de integración usando **JUnit 5** y **Mockito**.

Para ejecutar los tests:

```bash
mvn test
```

---

## 🛠️ Compilación y Despliegue

Para generar el artefacto `.jar` ejecutable:

```bash
mvn clean package
```

El archivo se generará en: `target/carnavawikiappback-0.1.7-SNAPSHOT.jar`

---

## 📄 Licencia

Proyecto desarrollado para fines educativos y de documentación cultural del Carnaval.
© 2025 CarnavaWiki – Todos los derechos reservados.
