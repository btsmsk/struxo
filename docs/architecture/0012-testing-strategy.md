# ADR-0012: Testing Strategy

**Status:** Accepted
**Date:** 2026-03-10

## Context

Testing in KMP requires different tools than Android-only projects. MockK uses bytecode manipulation incompatible with Kotlin/Native.

## Decision

Fake implementations in commonTest. kotlin.test + Turbine for assertions. Platform tests only when needed.

## Rules

**R1 — Fakes over mocks:**
Use hand-written Fake implementations (e.g., `FakeAuthRepository`) instead of MockK. Fakes work in commonTest for ALL targets.

**R2 — Test location:**
- `commonTest/`: All shared logic tests (UseCases, Repositories, ViewModels, Mappers)
- `androidTest/`: Android-specific tests only (Context-dependent code, instrumentation)
- `iosTest/`: iOS-specific tests only (NSUserDefaults, Keychain)

**R3 — Test framework:**
- `kotlin.test` for assertions (`assertEquals`, `assertTrue`, `assertIs`)
- `kotlinx-coroutines-test` for coroutine testing (`runTest`, `TestDispatcher`)
- `Turbine` for Flow assertions (`.test { awaitItem() }`)
- No JUnit4 `@Rule` in commonTest — use `Dispatchers.setMain()` / `resetMain()` directly

**R4 — Fake pattern:**
```kotlin
class FakeAuthRepository : AuthRepository {
    var loginResult: Resource<AuthUser> = Resource.Success(fakeAuthUser())
    override suspend fun login(email: String, password: String) = loginResult
}
```

**R5 — ViewModel test setup:**
```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setup() { Dispatchers.setMain(testDispatcher) }

    @AfterTest
    fun tearDown() { Dispatchers.resetMain() }
}
```

**R6 — Test naming:**
```kotlin
@Test
fun `login with valid credentials returns success`() = runTest { ... }
```
Pattern: `action_condition_expectedResult` in backtick format.

**R7 — Coverage targets:**
- `core/domain`: 70%+
- `core/data`: 70%+
- `feature/*/domain`: 80%+
- `feature/*/presentation`: 60%+

## Consequences

- All tests run on all platforms (no Kotlin/Native incompatibility)
- Fakes are more explicit and debuggable than mock frameworks
- Turbine provides structured Flow testing
