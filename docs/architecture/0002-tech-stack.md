# ADR-0002: Technology Stack

**Status:** Accepted
**Date:** 2026-03-10

## Context

Selecting a consistent technology stack for KMP that works across Android and iOS targets.

## Decision

Use the following stack exclusively. All versions managed via `gradle/libs.versions.toml`.

## Active Stack

| Category | Technology | Version |
|----------|-----------|---------|
| Language | Kotlin | 2.3.10 |
| UI | Compose Multiplatform | 1.10.2 |
| DI | Koin Multiplatform | 4.1.1 |
| Networking | Ktor Client | 3.4.1 |
| Local DB | Room Multiplatform | 2.8.4 |
| Serialization | Kotlinx Serialization | 1.8.1 |
| Coroutines | Kotlinx Coroutines | 1.10.2 |
| DateTime | Kotlinx DateTime | 0.7.1 |
| Navigation | Compose Navigation | 2.9.2 |
| Lifecycle | Lifecycle ViewModel Compose | 2.9.6 |
| Image Loading | Coil 3 | 3.4.0 |
| Logging | Napier | 2.7.1 |
| KSP | KSP | 2.3.5 |
| SQLite | SQLite Bundled | 2.6.2 |
| Testing | Kotlin Test + Turbine | 1.2.1 |
| AGP | Android Gradle Plugin | 9.1.0 |

## Rules

**R1 — Banned alternatives:**

| Banned | Reason | Use Instead |
|--------|--------|-------------|
| Hilt / Dagger | Android-only, no KMP support | Koin |
| Retrofit | Android-only, no KMP support | Ktor |
| Gson / Moshi | Not multiplatform | kotlinx.serialization |
| MockK | Bytecode manipulation breaks Kotlin/Native | Fake implementations |
| RxJava | Not multiplatform | Kotlin Coroutines + Flow |
| Glide / Picasso | Android-only | Coil 3 (KMP) |
| KAPT | Deprecated, slow | KSP only |
| Timber | Android-only | Napier |

**R2 — Version catalog only:**
All dependency versions MUST be in `gradle/libs.versions.toml`. No hardcoded version strings in build files.

**R3 — No upgrades without approval:**
Version changes require explicit user approval and must be verified on BOTH Android and iOS targets.

## Consequences

- Entire stack works across all KMP targets
- Single source of truth for versions prevents conflicts
- Banned list prevents accidental platform-only dependencies
