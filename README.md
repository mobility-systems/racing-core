# Racing Core API

The **Racing Core** is one of the microservices in the Mobility app.
It is the authoritative source of all racing domain data. From the car catalogue
(models, engines, and parts) to tracks, drivers, and lap records.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Architecture & Integrations](#architecture--integrations)
- [Key Features](#key-features)
- [Domain Overview](#domain-overview)
- [Security & Token Propagation](#security--token-propagation)
- [ETag & Optimistic Concurrency Control](#etag--optimistic-concurrency-control)
- [Caching](#caching)
- [AOP Logging](#aop-logging)
- [Error Handling](#error-handling)
- [API Endpoints Reference](#api-endpoints-reference)
- [Configuration](#configuration)
- [Prerequisites](#prerequisites)
- [Running the Service](#running-the-service)
- [Running Tests](#running-tests)
- [Shared Libraries](#shared-libraries)
- [Docker](#docker)

---

## Overview

Racing Core sits at the heart of the racing domain.
It manages the full lifecycle of car models, engines, parts, drivers,
tracks, and lap data. All write operations are restricted to privileged
administrators, while read operations are available publicly to any user.

The service integrates with the Account Management service over REST
to resolve user identity (for example, looking up a user ID by email
to assign the driver role). Communication with the Auth Server happens
over gRPC using the OAuth 2.1 client credentials flow to obtain access tokens
for outbound requests.

---

## Tech Stack

| Layer              | Technology                         |
|--------------------|------------------------------------|
| Language           | Java 21                            |
| Framework          | Spring Boot 3.5.6                  |
| Build Tool         | Maven                              |
| Database           | PostgreSQL (via Spring Data JPA)   |
| Schema  Migrations | Liquibase                          |
| Cache              | Redis                              |
| Inbound API        | REST (Spring MVC)                  |
| Outbound RPC       | gRPC                               |
| Object Mapping     | MapStruct                          |
| Auth Protocol      | OAuth 2.1 (Spring Resource Server) |
| API Docs           | SpringDoc OpenAPI (Swagger UI)     |
| AOP                | Spring AOP                         |

---

## Architecture & Integrations

**Auth Server** - Racing Core acts as an OAuth 2.1 Resource Server.
Every incoming request must carry a valid JWT Bearer token issued by the Auth Server.
Token validation is handled automatically by Spring Security.
Racing Core also communicates with the Auth Server over gRPC (using the client credentials flow)
to obtain access tokens for outbound service-to-service calls.
All outgoing gRPC calls are intercepted by `GrpcClientAuthInterceptor`,
which automatically fetches and attaches the token to the request metadata.

**Account Management Service** - Called over REST to resolve user identity data,
for example to fetch a user ID from an email address when assigning the driver role.

---

## Key Features

- **Full racing domain management:** car models, engines, parts, tracks, drivers, and lap records
- **Role-based access control:** write operations restricted to `SYS_ADMIN`, reads open to all users
- **ETag-based optimistic concurrency control:** on all mutating endpoints to prevent lost updates
- **gRPC client:** with automatic OAuth 2.1 bearer token injection for auth-server token acquisition
- **Redis caching:** for frequently read resources
- **AOP-based performance monitoring:** automatic slow-method detection across service and repository layers
- **MapStruct:** for clean, performant, and type-safe mapping
- **Centralized exception handling:** with structured error responses

---

## Domain Overview

| Resource   | Description                                       |
|------------|---------------------------------------------------|
| Car Models | Vehicle model data (make, specs, classifications) |
| Engines    | Engine configurations and metadata                |
| Parts      | Individual car components and part catalogue      |
| Tracks     | Race circuit data and layout information          |
| Drivers    | Driver profiles linked to user identities         |
| Laps       | Lap timing and telemetry records per driver/track |

---

## Security & Token Propagation

Racing Core is an **OAuth 2.1 Resource Server**. All endpoints require a valid JWT
Bearer token issued by the Auth Server.

### Roles

| Role          | Description                                             |
|---------------|---------------------------------------------------------|
| `SYS_ADMIN`   | Full access to create, update, and delete all resources |
| `SIMPLE_USER` | To be able to become a DRIVER                           |

### Outbound gRPC Authentication

Racing Core calls the Auth Server over gRPC to obtain access tokens
for outbound service-to-service calls using the OAuth 2.1 client credentials flow.

The `GrpcClientAuthInterceptor` is registered as a global gRPC client interceptor
and transparently:

1. Requests an access token from the Auth Server using the `mobility-api` client registration.
2. Attaches the token as a `Bearer` value in the gRPC `Authorization` metadata header.

This happens automatically for every outbound gRPC call without any manual token management.

---

## ETag & Optimistic Concurrency Control

All mutating endpoints (`PUT`, `DELETE`) support **optimistic concurrency control**
via the standard HTTP `If-Match` header.

The client includes the ETag value received from a previous GET response.
If the resource has been modified in the meantime, the server rejects the
request with `412 Precondition Failed`, preventing lost updates in concurrent scenarios.

```
GET  /cars/model/by-id/1
     --> Response: ETag: "3"

PUT  /cars/model/update/1
     --> Request Header: If-Match: "3"
     --> If version matches:   200 OK with updated ETag
     --> If version mismatch:  412 Precondition Failed
```

---

## Caching

Redis is used to cache frequently read resources and reduce database load.
Cache entries are scoped by resource identifier and null results are
explicitly excluded from caching.

---

## AOP Logging

A custom `LoggingAspect` monitors all service and repository layer methods using Spring AOP.
Methods that exceed a threshold (which is determined by a configuration)
are automatically flagged with a `[SLOW]` warning log:

```
WARN  CarService - CarService.findCarModelById() took 1345ms [SLOW]
```

This provides lightweight, zero-boilerplate performance observability
across the entire service layer without modifying business logic.

---

## Error Handling

All exceptions are handled centrally by `RacingCoreExceptionsHandler` (`@RestControllerAdvice`),
which returns consistent `MobilityAppErrorResponse` JSON bodies with a human-readable
message and a timestamp.

`MobilityAppErrorResponse` example:

```json
{
  "timestamp": "2026-02-20T10:15:30Z",
  "message": "Car model not found"
}
```

### HTTP Status Codes

| Exception                         | HTTP Status             | Response Message                              |
|-----------------------------------|-------------------------|-----------------------------------------------|
| `MethodArgumentNotValidException` | 400 Bad Request         | Field-level validation errors joined together |
| `IllegalArgumentException`        | 400 Bad Request         | ex.getMessage()                               |
| `NotFoundException`               | 404 Not Found           | ex.getMessage()                               |
| `AlreadyExistsException`          | 409 Conflict            | ex.getMessage()                               |
| `InvalidETagException`            | 412 Precondition Failed | Entity Tag validation failed                  |

---

## API Endpoints Reference

Full interactive documentation is available via **Swagger UI** at
`http://localhost:8086/motorsport/swagger-ui/index.html` when running locally.

---

## Configuration

The service is configured via `appl;ication.properties` and environment variables.
Environment specific overrides are applied via Spring profiles (`local`, `staging`, `prod`),
activated either through Maven profile flags or by setting `spring.profiles.active` directly.

### Maven Profiles

| Profile ID | Usage                                                    |
|------------|----------------------------------------------------------|
| `local`    | Local development --> `application-local.properties`     |
| `staging`  | Staging environment --> `application-staging.properties` |
| `prod`     | Production environment --> `application-prod.properties` |

Activate a profile at build or run time:

```bash
# build
mvn spring-boot:run -Plocal

# run
java -jar target/racing-core.jar --spring.profiles.active=staging
```

---

## Prerequisites

- **Java 21+**
- **Maven 3.9+**
- **Docker**
- **PostgreSQL**
- **Redis**

---

## Running the Service

```bash
# build
mvn clean package -DskipTests

# run
java -jar target/racing-core.jar
```

Or with Maven directly:

```bash
mvn spring-boot:run -Plocal
```

---

## Logging

The application uses a custom `log4j2-spring.xml` configuration supporting
Spring profile specific log levels and output formatting.

```
src/main/resources/log4j2-spring.xml
```

---

## Running Tests

### Unit Tests

Unit tests run automatically and exclude integration tests (`*IT.java`).

```bash
mvn test
```

### Integration Tests

Integration tests use **Testcontainers** and spin up a real PostgreSQL instance automatically.
No manual database setup required, and they are designed to be as close as possible to end-to-end testing.

```bash
mvn verify -P integration
```

---

## Shared Libraries

This service depends on two internal libraries hosted on GitHub Packages:

| Library                 | Purpose                                                 |
|-------------------------|---------------------------------------------------------|
| `infrastructure-common` | Shared infrastructure utilities and base configurations |
| `proto-common`          | Protobuf / gRPC service definitions                     |

These must be available in the local Maven repository or a private artifact
registry before building.

---

## Docker

Start the required infrastructure services with Docker Compose:

```bash
docker compose up -d
```

> **Current state:** The provided `docker-compose.yml` starts only the PostgreSQL database.
> Redis and full service containerization are in progress.

> [!WARNING]
> **Make sure all required infrastructure services (PostgreSQL, Redis) are
> available and healthy before starting the application.**
