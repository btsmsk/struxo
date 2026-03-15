# ADR-0007: Dependency Injection

**Status:** Accepted
**Date:** 2026-03-10

## Context

Need DI that works across KMP targets. Hilt is Android-only and incompatible with KMP.

## Decision

Use Koin Multiplatform with module{} blocks in commonMain and platform modules via expect/actual.

## Rules

**R1 — No Hilt / Dagger / @Inject:**
Never use `@Inject`, `@HiltViewModel`, `@Module`, `@InstallIn`, `@Provides`, `@Binds`, `@Singleton`. Use Koin's DSL instead.

**R2 — Koin module structure:**
```kotlin
// commonMain
val coreModule = module {
    single { ApiClient(get(), get()) }
    single { AppDatabase.build(get()) }
}

val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    factory { LoginUseCase(get()) }
    viewModel { LoginViewModel(get()) }
}

val appModules = listOf(coreModule, authModule, platformModule)
```

**R3 — Platform module via expect/actual:**
```kotlin
// commonMain
expect val platformModule: Module

// androidMain
actual val platformModule = module {
    single<HttpClientEngine> { OkHttp.create() }
    single { getDatabaseBuilder(androidContext()) }
}

// iosMain
actual val platformModule = module {
    single<HttpClientEngine> { Darwin.create() }
    single { getDatabaseBuilder() }
}
```

**R4 — ViewModel injection in Compose:**
Use `koinViewModel()` in composables:
```kotlin
@Composable
fun LoginScreen(viewModel: LoginViewModel = koinViewModel()) { ... }
```

**R5 — No ServiceLocator / static instances:**
Never use `companion object { var instance }` patterns. All dependencies via Koin graph.

**R6 — Koin initialization:**
- Android: `startKoin { androidContext(this@App); modules(appModules) }` in Application class
- iOS: `startKoin { modules(appModules) }` via KoinHelper called from Swift

## Consequences

- DI works identically across all KMP targets
- Module structure mirrors Clean Architecture layers
- Platform-specific dependencies are isolated in platformModule
