# ADR-0003: State Management

**Status:** Accepted
**Date:** 2026-03-10

## Context

Need a consistent state management pattern for ViewModels that works in Compose Multiplatform across Android and iOS.

## Decision

MVI pattern using `BaseViewModel<State, Event, Effect>` with StateFlow for state and Channel for one-time effects.

## Rules

**R1 — BaseViewModel contract:**
```kotlin
abstract class BaseViewModel<S, E, F>(initialState: S) : ViewModel() {
    val uiState: StateFlow<S>       // Observable UI state
    val effect: Flow<F>             // One-time side effects via Channel
    abstract fun onEvent(event: E)  // Event handler
    fun updateState { copy(...) }   // State reducer
    fun sendEffect(effect: F)       // Emit side effect
}
```

**R2 — Per-feature State data class:**
Each feature defines its own State data class (not a generic ScreenUiState<T>):
```kotlin
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
)
```

**R3 — No Context in ViewModel:**
ViewModels live in `commonMain`. No `android.content.Context`, no `platform.*` types.

**R4 — One-shot events use Channel:**
Never `MutableSharedFlow` for events. Always `Channel<F>(Channel.BUFFERED)` with `receiveAsFlow()`.

**R5 — UI state collection:**
Always `collectAsStateWithLifecycle()` in Compose — never bare `collectAsState()`.

**R6 — Event/Effect sealed interfaces:**
```kotlin
sealed interface LoginEvent {
    data class EmailChanged(val value: String) : LoginEvent
    data object LoginClicked : LoginEvent
}
sealed interface LoginEffect {
    data object NavigateToHome : LoginEffect
    data class ShowError(val message: String) : LoginEffect
}
```

**R7 — No GlobalScope:**
Only `viewModelScope` for coroutine launching. Never `GlobalScope.launch`.

## Consequences

- Per-feature State is more expressive for form screens than generic wrappers
- Channel guarantees one-time effect delivery
- Pure Kotlin ViewModel works identically on Android and iOS
