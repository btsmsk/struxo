# ADR-0001: Architecture Overview

**Status:** Accepted
**Date:** 2026-03-10

## Context

Struxo is a Kotlin Multiplatform starter template targeting Android and iOS. We need a scalable, testable architecture that works identically across platforms with clear separation of concerns.

## Decision

Adopt **Clean Architecture + MVI** with KMP source set boundaries.

## Rules

**R1 — Three layers with strict boundaries:**
- Domain: pure Kotlin, zero platform dependencies
- Data: implements domain interfaces, Ktor remote + Room local
- Presentation: BaseViewModel<State,Event,Effect> + Compose Multiplatform UI

**R2 — Domain purity:**
Domain layer (`core/domain/`, `feature/*/domain/`) must have ZERO imports from:
- `android.*`, `androidx.*` (Android)
- `platform.*` (iOS/Kotlin Native)
- `io.ktor.*`, `androidx.room.*` (framework)
Only pure Kotlin + kotlinx libraries allowed.

**R3 — Data layer encapsulation:**
DTOs and Room entities never leak to presentation. Repository implementations map to domain entities before returning.

**R4 — Dependency direction:**
Presentation → Domain ← Data. Presentation never imports from Data. Data never imports from Presentation.

**R5 — Feature isolation:**
Feature modules depend on `core/*` only. Never import from another feature package.

**R6 — Source set rules (AGP 9 two-module architecture):**
- `commonMain`: All shared code (domain, data interfaces, presentation, UI)
- `androidMain`: Android-specific implementations (OkHttp, Room builder with Context, EncryptedSharedPreferences)
- `iosMain`: iOS-specific implementations (Darwin engine, Room builder with NSHomeDirectory, Keychain)
- `androidApp/`: Android entry point (MainActivity, AndroidManifest)
- `iosApp/`: iOS entry point (SwiftUI wrapper)

**R7 — expect/actual completeness:**
Every `expect` declaration in `commonMain` MUST have `actual` implementations in BOTH `androidMain` AND `iosMain`. No exceptions.

## Consequences

- Clear boundaries prevent architectural erosion across 9+ apps
- expect/actual enforces conscious platform decisions
- Two-module AGP 9 architecture is future-proof
