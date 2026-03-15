# ADR-0014: iOS Platform

**Status:** Accepted
**Date:** 2026-03-10

## Context

iOS-specific conventions for the Struxo KMP project, covering the iosApp module and iosMain source set.

## Decision

Document all iOS-specific patterns that differ from commonMain shared code.

## Rules

**R1 — iosApp module (entry point):**
```
iosApp/
├── iosApp/
│   ├── iOSApp.swift          # @main App struct
│   └── ContentView.swift     # UIViewControllerRepresentable wrapping ComposeUIViewController
└── iosApp.xcodeproj/
```

**R2 — iosMain source set (platform implementations):**
```
composeApp/src/iosMain/kotlin/com/struxo/kit/
├── di/PlatformModule.ios.kt
├── core/data/local/DatabaseBuilder.ios.kt
└── core/util/Platform.ios.kt
```

**R3 — Darwin engine:**
```kotlin
actual val platformModule = module {
    single<HttpClientEngine> { Darwin.create() }
}
```

**R4 — Room DatabaseBuilder with NSHomeDirectory:**
```kotlin
fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbPath = NSHomeDirectory() + "/databases/" + AppDatabase.DB_NAME
    return Room.databaseBuilder<AppDatabase>(name = dbPath)
}
```

**R5 — Token storage via Keychain:**
```kotlin
actual class TokenProviderImpl : TokenProvider {
    // Use Security framework (SecItemAdd, SecItemCopyMatching, etc.)
}
```

**R6 — Napier initialization:**
```kotlin
Napier.base(DebugAntilog()) // Called from KoinHelper.doInitKoin()
```

**R7 — Framework export:**
```kotlin
// In composeApp/build.gradle.kts
iosTarget.binaries.framework {
    baseName = "ComposeApp"
    isStatic = true
}
```

**R8 — MainViewController (iosMain):**
```kotlin
fun MainViewController() = ComposeUIViewController { App() }
```

**R9 — Koin initialization for iOS:**
```kotlin
// iosMain — called from Swift
object KoinHelper {
    fun doInitKoin() {
        startKoin { modules(appModules) }
    }
}
```
Swift side: `KoinHelper.shared.doInitKoin()` in `iOSApp.init()`.

**R10 — Kotlin/Native considerations:**
- No `@JvmStatic`, `@JvmOverloads` in shared code
- Frozen objects are deprecated but be aware of threading model differences
- Object declarations are fine for singletons

## Consequences

- iOS entry point is a thin SwiftUI wrapper around ComposeUIViewController
- All platform implementations isolated in iosMain
- Framework export configured for static linking (recommended for Compose Multiplatform)
