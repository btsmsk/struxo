# ADR-0013: Android Platform

**Status:** Accepted
**Date:** 2026-03-10

## Context

Android-specific conventions for the Struxo KMP project, covering the androidApp module and androidMain source set.

## Decision

Document all Android-specific patterns that differ from commonMain shared code.

## Rules

**R1 — androidApp module (entry point):**
```
androidApp/
├── src/main/
│   ├── kotlin/com/struxo/kit/
│   │   └── MainActivity.kt    # setContent { App() }
│   └── AndroidManifest.xml
└── build.gradle.kts           # com.android.application plugin
```
MainActivity does nothing except call the shared `App()` composable.

**R2 — androidMain source set (platform implementations):**
```
composeApp/src/androidMain/kotlin/com/struxo/kit/
├── di/PlatformModule.android.kt
├── core/data/local/DatabaseBuilder.android.kt
└── core/util/Platform.android.kt
```

**R3 — OkHttp engine:**
```kotlin
actual val platformModule = module {
    single<HttpClientEngine> { OkHttp.create() }
}
```

**R4 — Room DatabaseBuilder with Context:**
```kotlin
fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> =
    Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DB_NAME)
```

**R5 — Token storage via EncryptedSharedPreferences:**
```kotlin
actual class TokenProviderImpl(context: Context) : TokenProvider {
    private val prefs = EncryptedSharedPreferences.create(...)
}
```

**R6 — Napier initialization:**
```kotlin
Napier.base(DebugAntilog()) // In Application.onCreate() or MainActivity
```

**R7 — Android SDK targets:**
- minSdk: 26
- compileSdk: 35
- targetSdk: 35

**R8 — No Hilt:**
Use `androidContext()` in Koin for Context injection. Never `@HiltAndroidApp`, `@AndroidEntryPoint`, or `@HiltViewModel`.

**R9 — Koin initialization on Android:**
```kotlin
class StruxoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@StruxoApplication)
            modules(appModules)
        }
    }
}
```

## Consequences

- Android entry point is minimal — all logic in shared code
- Platform implementations isolated in androidMain
- Koin replaces Hilt for all DI needs
