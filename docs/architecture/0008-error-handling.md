# ADR-0008: Error Handling

**Status:** Accepted
**Date:** 2026-03-10

## Context

Need a unified error handling strategy that works across all KMP targets with consistent error propagation from data to presentation layer.

## Decision

Single `Resource<T>` sealed class with `safeCall{}` wrapper. No separate `Result<T>` + `ApiResponse<T>`.

## Rules

**R1 — Resource<T> sealed class (commonMain):**
```kotlin
sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : Resource<Nothing>()
}
```

**R2 — safeCall{} wrapper:**
```kotlin
suspend fun <T> safeCall(block: suspend () -> T): Resource<T> = try {
    Resource.Success(block())
} catch (e: Exception) {
    Resource.Error(e.message ?: "Unknown error", e)
}
```

**R3 — All data operations return Resource<T>:**
Repository methods, UseCase invoke, and ViewModel state updates all use Resource<T>.

**R4 — No try/catch in ViewModel:**
Error handling happens in the data layer via safeCall{}. ViewModel receives Resource.Error and maps to UI state.

**R5 — Network errors show retry + local fallback:**
When remote call fails, show cached data if available + retry option.

**R6 — Extension helpers:**
```kotlin
val Resource<*>.isLoading get() = this is Resource.Loading
val Resource<*>.isSuccess get() = this is Resource.Success
fun <T> Resource<T>.getOrNull(): T? = (this as? Resource.Success)?.data
fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R>
```

## Consequences

- Single sealed class reduces mapping boilerplate vs dual Result/ApiResponse
- safeCall{} prevents exception leaks across layer boundaries
- Consistent error propagation from data → domain → presentation
