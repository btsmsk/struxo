# ADR-0004: Navigation

**Status:** Accepted
**Date:** 2026-03-10

## Context

Need type-safe navigation that works across Compose Multiplatform for both Android and iOS.

## Decision

Use Compose Navigation multiplatform (`org.jetbrains.androidx.navigation`) with `@Serializable` route definitions.

## Rules

**R1 — Shared NavHost in commonMain:**
Navigation graph is defined once in `commonMain` and used by both platforms.

**R2 — Type-safe routes:**
All routes are `@Serializable data object` (no params) or `@Serializable data class` (with params):
```kotlin
@Serializable data object LoginRoute
@Serializable data class DetailRoute(val id: String)
```
No raw string route constants.

**R3 — Auth flow navigation:**
After login success: `popUpTo(0) { inclusive = true }` to clear auth back stack.

**R4 — Feature screen registration:**
Each feature's screen is registered in the central `AppNavigation.kt` NavHost.

**R5 — No platform-specific navigation:**
Navigation logic is entirely in `commonMain`. Platform entry points just call `App()`.

## Consequences

- Single navigation graph for both platforms
- Type-safe routes prevent runtime navigation errors
- Serializable routes support argument passing natively
