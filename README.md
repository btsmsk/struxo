# Struxo

[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.10-7F52FF?logo=kotlin)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.10.2-4285F4)](https://www.jetbrains.com/compose-multiplatform/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20iOS-blue)]()

Production-ready Kotlin Multiplatform starter template with **Clean Architecture + MVI + Compose Multiplatform** for Android and iOS. Designed as a reusable foundation for building cross-platform apps.

## Features

| Feature | Status |
|---------|--------|
| Clean Architecture (Domain / Data / Presentation) | Done |
| MVI pattern with BaseViewModel | Done |
| Compose Multiplatform UI | Done |
| Koin dependency injection | Done |
| Ktor networking with auth token management | Done |
| Room Multiplatform local database | Done |
| Material 3 theming with design tokens | Done |
| Type-safe Compose Navigation | Done |
| Offline-first data strategy | Done |
| Auth feature (Login) as reference implementation | Done |
| Reusable UI components (TextField, LoadingButton, ErrorView, EmptyState) | Done |
| CI/CD with GitHub Actions (Android + iOS) | Done |
| 14 Architecture Decision Records | Done |

## Architecture

```
┌──────────────────────────────────────────────────┐
│                  Presentation                     │
│  Screen (Compose) → ViewModel (MVI) → UseCase     │
├──────────────────────────────────────────────────┤
│                    Domain                         │
│  Entities ← Repository Interface ← UseCase        │
│  (pure Kotlin, zero platform dependencies)        │
├──────────────────────────────────────────────────┤
│                     Data                          │
│  RepositoryImpl → Remote (Ktor) + Local (Room)    │
│  DTOs mapped to domain entities at boundary       │
└──────────────────────────────────────────────────┘
```

**MVI Flow:**
`UI Event → ViewModel.onEvent() → UseCase → Repository → Resource<T> → State update → UI recompose`

One-time effects (navigation, snackbar) flow via `Channel`, not `SharedFlow`.

## Project Structure

```
struxo/
├── androidApp/                          # Android entry point
│   └── src/main/kotlin/.../
│       ├── MainActivity.kt
│       └── StruxoApplication.kt         # startKoin + Napier
│
├── composeApp/src/                      # Shared KMP library
│   ├── commonMain/kotlin/com/struxo/kit/
│   │   ├── core/
│   │   │   ├── data/
│   │   │   │   ├── local/               # Room DB, DAOs, entities
│   │   │   │   └── network/             # ApiClient, TokenProvider, HttpEngineFactory
│   │   │   ├── domain/usecase/          # UseCase base classes
│   │   │   ├── presentation/
│   │   │   │   ├── base/               # BaseViewModel<State, Event, Effect>
│   │   │   │   ├── components/         # AppTextField, LoadingButton, ErrorView, EmptyState
│   │   │   │   ├── navigation/         # Routes, AppNavigation
│   │   │   │   └── theme/             # Color, Type, Spacing, AppTheme
│   │   │   └── util/                   # Resource<T>, DateTimeUtil
│   │   ├── di/                         # AppModule, PlatformModule (expect)
│   │   └── feature/
│   │       ├── auth/                   # Full MVI reference: domain → data → presentation
│   │       └── home/                   # Minimal post-login placeholder
│   │
│   ├── commonTest/                     # Shared tests (Fakes + Turbine)
│   ├── androidMain/                    # OkHttp, Room builder, SharedPreferences token storage
│   └── iosMain/                        # Darwin, Room builder, NSUserDefaults token storage, KoinHelper
│
├── iosApp/                             # iOS entry point (SwiftUI)
│   └── iosApp/
│       ├── iOSApp.swift                # KoinHelper.shared.doInitKoin()
│       └── ContentView.swift
│
├── docs/architecture/                  # 14 ADRs (0001–0014)
├── gradle/libs.versions.toml          # All dependency versions
└── .github/workflows/ci.yml          # CI pipeline
```

## Tech Stack

| Category | Library | Version |
|----------|---------|---------|
| Language | Kotlin | 2.3.10 |
| UI | Compose Multiplatform | 1.10.2 |
| DI | Koin | 4.1.1 |
| Networking | Ktor | 3.4.1 |
| Local DB | Room Multiplatform | 2.8.4 |
| Serialization | kotlinx.serialization | 1.8.1 |
| Coroutines | kotlinx.coroutines | 1.10.2 |
| Date/Time | kotlinx-datetime | 0.7.1 |
| Navigation | Compose Navigation | 2.9.2 |
| ViewModel | Lifecycle ViewModel | 2.9.6 |
| Image Loading | Coil 3 | 3.4.0 |
| Logging | Napier | 2.7.1 |
| Flow Testing | Turbine | 1.2.1 |
| Build System | AGP | 9.1.0 |

## Quick Start

### Prerequisites

- **JDK 17+**
- **Android Studio** (latest stable) with KMP plugin
- **Xcode 16+** (for iOS)

### Setup

```bash
git clone <repository-url>
cd struxo
```

### Run Android

```bash
./gradlew :androidApp:assembleDebug
```

Or open in Android Studio and run the `androidApp` configuration.

### Run iOS

```bash
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64
```

Then open `iosApp/iosApp.xcodeproj` in Xcode and run on a simulator.

### Run Tests

```bash
# All platforms
./gradlew :composeApp:allTests

# Android only
./gradlew :composeApp:testDebugUnitTest

# iOS simulator only
./gradlew :composeApp:iosSimulatorArm64Test
```

## Architecture Guide

### Layers

- **Domain** — Pure Kotlin. Entities, repository interfaces, and UseCases. Zero platform dependencies.
- **Data** — Implements repositories. Coordinates Ktor remote sources and Room local sources. DTOs never leak past this layer.
- **Presentation** — `BaseViewModel<State, Event, Effect>` with MVI. Compose screens split into stateful (`{Feature}Screen`) and stateless (`{Feature}Content`) composables.

### Key Patterns

- **`Resource<T>`** — Sealed class (`Loading` / `Success` / `Error`) wrapping all data operations
- **`safeCall {}`** — Suspend wrapper for automatic error catching in repositories
- **`UseCase<P, R>`** — Single-responsibility operators with `invoke()`, called by ViewModels (never repositories directly)
- **`expect/actual`** — Platform-specific implementations for HTTP engines, database builders, and token storage

### Adding a Feature

Follow the [Sacred Implementation Order](CLAUDE.md):

1. Domain Entity → 2. Repository Interface → 3. UseCase → 4. DTO → 5. ApiClient helpers → 6. RepositoryImpl → 7. ViewModel → 8. Screen → 9. DI module → 10. Navigation route → 11. expect/actual

See `feature/auth/` for a complete reference implementation.

## Platform Targets

- **Android**: minSdk 26, compileSdk 35, targetSdk 35
- **iOS**: iosX64, iosArm64, iosSimulatorArm64

## Architecture Decision Records

14 ADRs document every architectural decision. See [`docs/architecture/`](docs/architecture/).

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

## License

[MIT](LICENSE) — Copyright (c) 2026 Batu
