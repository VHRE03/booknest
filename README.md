# BookNest

API REST para la gestión de una librería (**autores**, **libros** y **reseñas**) construida como un proyecto **standalone**: mantiene la arquitectura modular del `base-project` pero **sin depender de él** — no hay clases base genéricas ni el starter `com.vhre.base`; cada módulo contiene explícitamente sus propias capas.

## Stack

| Componente     | Tecnología                                     |
|----------------|------------------------------------------------|
| Lenguaje       | Java 21                                        |
| Framework      | Spring Boot 3.3.4 (Web, Data JPA, Validation)  |
| Base de datos  | PostgreSQL 16 (Docker)                         |
| Mapeo          | MapStruct 1.5.5                                |
| Documentación  | SpringDoc OpenAPI 3 / Swagger UI               |
| Utilidades     | Lombok                                         |
| Tests          | JUnit 5 + H2 en memoria                        |

## Arquitectura

Cada módulo (`author`, `book`, `review`) es autónomo y contiene sus capas completas:

```
src/main/java/com/vhre/booknest/
├── BookNestApplication.java          # Punto de entrada
├── common/
│   └── exception/                     # Manejador de errores basico comun
│       ├── ApiErrorResponse.java      #   Cuerpo de error estandar (timestamp, status, error, details, path)
│       ├── ResourceNotFoundException.java
│       ├── DuplicateResourceException.java
│       └── GlobalExceptionHandler.java    # @RestControllerAdvice: 400/404/409/500
├── config/
│   └── OpenApiConfig.java            # Documentacion Swagger/OpenAPI
└── modules/
    ├── author/
    │   ├── controller/AuthorController.java
    │   ├── dto/AuthorDTO.java
    │   ├── entity/Author.java
    │   ├── enums/Nationality.java
    │   ├── mapper/AuthorMapper.java      # MapStruct: Entity <-> DTO
    │   ├── repository/AuthorRepository.java
    │   └── service/
    │       ├── AuthorService.java         # Contrato
    │       └── AuthorServiceImpl.java    # Implementacion
    ├── book/                          # (misma estructura + BookGenre, BookLanguage)
    └── review/                        # (misma estructura)
```

## Modelo de datos

```
┌──────────────┐           ┌──────────────┐           ┌──────────────┐
│   AUTHORS    │1         *│    BOOKS     │1         *│   REVIEWS     │
│──────────────│──────────▶│──────────────│──────────▶│──────────────│
│ id (uuid)    │           │ id (uuid)    │           │ id (uuid)    │
│ full_name    │           │ title        │           │ reviewer_name│
│ pseudonym    │           │ isbn (uniq)  │           │ rating (1-5) │
│ email (uniq) │           │ synopsis TEXT│           │ comment TEXT │
│ biography TXT│           │ genre (enum) │           │ would_recommend│
│ birth_date   │           │ price numeric│          │ helpful_votes│
│ nationality  │           │ published_date│          │ book_id (FK) │
│ awards (int) │           │ pages (int)  │           │ created_at   │
│ is_active    │           │ stock (int)  │           │ updated_at   │
│ created_at   │           │ is_available │           └──────────────┘
│ updated_at   │           │ language(enum)│
└──────────────┘           │ author_id FK │
                           │ created_at   │
                           │ updated_at   │
                           └──────────────┘
```

**Relaciones:** `Author 1 ── N Book` y `Book 1 ── N Review`. Las relaciones viajan por referencia en los DTOs (`authorId`, `bookId`); el service valida que existan y las resuelve.

**Variedad de tipos de columnas:** `uuid`, `varchar(n)`, `text`, `date`, `timestamp`, `numeric(10,2)`, `integer`, `boolean` y enums persistidos como `string` con restricción `CHECK`.

## Ejecución

### 1. Levantar PostgreSQL (Docker)

```bash
docker compose up -d
```

### 2. Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

### 3. Documentación Swagger

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

### Tests

```bash
./mvnw test    # Usa H2 en memoria (modo PostgreSQL), no requiere Docker
```

## Endpoints

| Método  | Ruta                              | Descripción                              |
|---------|-----------------------------------|------------------------------------------|
| GET     | `/api/v1/authors`                 | Listado paginado (`?page&size&sort`)    |
| GET     | `/api/v1/authors/{id}`            | Autor por ID                             |
| POST    | `/api/v1/authors`                 | Crear autor (email único)               |
| PUT     | `/api/v1/authors/{id}`            | Actualizar autor                         |
| DELETE  | `/api/v1/authors/{id}`            | Eliminar autor (409 si tiene libros)    |
| GET     | `/api/v1/books`                   | Listado paginado de libros              |
| GET     | `/api/v1/books/{id}`              | Libro por ID                             |
| GET     | `/api/v1/books/author/{authorId}` | Libros de un autor                       |
| POST    | `/api/v1/books`                    | Crear libro (ISBN único, autor válido)  |
| PUT     | `/api/v1/books/{id}`              | Actualizar libro                         |
| DELETE  | `/api/v1/books/{id}`              | Eliminar libro (409 si tiene reseñas)   |
| GET     | `/api/v1/reviews`                  | Listado paginado de reseñas             |
| GET     | `/api/v1/reviews/{id}`            | Reseña por ID                            |
| GET     | `/api/v1/reviews/book/{bookId}`   | Reseñas de un libro                      |
| POST    | `/api/v1/reviews`                 | Crear reseña (libro válido)             |
| PUT     | `/api/v1/reviews/{id}`            | Actualizar reseña                        |
| DELETE  | `/api/v1/reviews/{id}`            | Eliminar reseña                          |

## Manejo de errores

Todas las respuestas de error comparten el mismo cuerpo:

```json
{
  "timestamp": "2026-10-14T11:33:51",
  "status": 404,
  "error": "Not Found",
  "details": ["Author not found with ID: 00000000-..."],
  "path": "/api/v1/authors/00000000-..."
}
```

| HTTP | Causa                                                              |
|------|--------------------------------------------------------------------|
| 400  | Validación de DTO (`@Valid`) o JSON malformado / UUID inválido      |
| 404  | Recurso inexistente (o relación referenciada inexistente)          |
| 409  | Regla de negocio: email/ISBN duplicado o integridad referencial FK |
| 500  | Error inesperado (mensaje genérico, sin filtrar el stack trace)    |

## Variables de entorno (opcionales)

| Variable       | Default           |
|----------------|-------------------|
| SERVER_PORT    | 8080              |
| DB_HOST        | localhost         |
| DB_PORT        | 5432              |
| DB_NAME        | booknest_db       |
| DB_USER        | booknest_user     |
| DB_PASSWORD    | booknest_pass     |
