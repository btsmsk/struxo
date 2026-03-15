# ADR-0005: Networking

**Status:** Accepted
**Date:** 2026-03-10

## Context

Need a multiplatform HTTP client that works identically on Android and iOS with auth, logging, and error handling.

## Decision

Use Ktor HttpClient with platform-specific engines via expect/actual + Koin DI.

## Rules

**R1 — Ktor HttpClient configuration (commonMain):**
```kotlin
HttpClient(engine) {
    install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true; coerceInputValues = true }) }
    install(Auth) { bearer { loadTokens { ... }; refreshTokens { ... } } }
    install(Logging) { logger = NapierLogger; level = LogLevel.BODY }
    install(HttpTimeout) { requestTimeoutMillis = 30_000; connectTimeoutMillis = 15_000 }
    defaultRequest { url(baseUrl); contentType(ContentType.Application.Json) }
}
```

**R2 — Platform engine selection via expect/actual + Koin:**
- `androidMain`: OkHttp engine
- `iosMain`: Darwin engine
Engine provided via Koin platform module.

**R3 — No Retrofit:**
Ktor's typed helper methods (`get<T>()`, `post<T>()`) replace Retrofit's annotation-based approach.

**R4 — DTOs are @Serializable:**
All network DTOs use `kotlinx.serialization.Serializable`. No Gson, no Moshi.

**R5 — DTOs never leak:**
Repository converts DTO → domain entity before returning. Presentation never sees DTOs.

**R6 — safeCall{} wrapping:**
All suspend network calls wrapped in `safeCall {}` which catches exceptions and returns `Resource.Error`.

**R7 — No direct 401 handling in features:**
Auth token refresh handled by Ktor Auth plugin globally.

## Consequences

- Identical networking code runs on both platforms
- Platform engine selection is the only expect/actual needed for networking
- safeCall{} provides consistent error handling across all network operations
