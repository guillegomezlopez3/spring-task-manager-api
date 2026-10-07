# spring-task-manager-api

Task manager REST API built with **Spring Boot 3** and **Java 21**. It is a small, clean example of a layered backend: controller, service, repository and DTOs, with validation, error handling and pagination.

## Features

- CRUD for tasks (`title`, `description`, `status`, `dueDate`)
- Status filter (`TODO`, `IN_PROGRESS`, `DONE`) and pagination
- Request validation with Bean Validation and JSON error responses
- 404 handling through a `@RestControllerAdvice`
- H2 in-memory by default, MySQL with the `mysql` profile
- MockMvc integration tests

## Stack

Java 21 · Spring Boot 3.3 · Spring Data JPA · H2 / MySQL · Maven · JUnit 5

## Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/tasks?status=&page=&size=` | List tasks (paginated, optional status filter) |
| GET | `/api/tasks/{id}` | Get a task |
| POST | `/api/tasks` | Create a task |
| PUT | `/api/tasks/{id}` | Update a task |
| DELETE | `/api/tasks/{id}` | Delete a task |

## Run

```bash
mvn spring-boot:run
curl -X POST localhost:8080/api/tasks -H 'Content-Type: application/json' \
  -d '{"title":"Learn Spring Security","dueDate":"2026-12-01"}'
curl 'localhost:8080/api/tasks?status=TODO'
```

With MySQL:

```bash
DB_USER=root DB_PASSWORD=secret mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

## Tests

```bash
mvn test
```

## Project structure

```
controller/   REST endpoints and error handling
service/      business logic (transactional)
repository/   Spring Data JPA repositories
model/        JPA entities and enums
dto/          request objects with validation
```
