package com.struxo.kit.core.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * MVI base ViewModel for all feature ViewModels.
 *
 * Provides a unidirectional data flow:
 * - **State** ([uiState]) — observable UI state via [StateFlow].
 * - **Event** — user actions dispatched through [onEvent].
 * - **Effect** ([effect]) — one-time side effects (navigation, snackbar) via [Channel].
 *
 * @param S The UI state type (should be a data class with `copy()`).
 * @param E The event/intent sealed interface.
 * @param F The one-time effect sealed interface.
 * @param initialState The initial UI state.
 */
abstract class BaseViewModel<S, E, F>(initialState: S) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)

    /** Observable UI state. Collect with `collectAsStateWithLifecycle()` in Compose. */
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    /** Snapshot of the current state. */
    val currentState: S get() = _uiState.value

    private val _effect = Channel<F>(Channel.BUFFERED)

    /** One-time side effects. Collect inside a `LaunchedEffect` in the Screen composable. */
    val effect = _effect.receiveAsFlow()

    /**
     * Handles a UI event.
     *
     * @param event The user action to process.
     */
    abstract fun onEvent(event: E)

    /**
     * Updates the UI state atomically.
     *
     * @param reduce Lambda that receives the current state and returns the new state.
     */
    protected fun updateState(reduce: S.() -> S) {
        _uiState.update(reduce)
    }

    /**
     * Emits a one-time side effect.
     *
     * @param effectValue The effect to send.
     */
    protected fun sendEffect(effectValue: F) {
        viewModelScope.launch { _effect.send(effectValue) }
    }

    /**
     * Launches a coroutine in [viewModelScope] with error handling.
     *
     * Exceptions are forwarded to [onError] instead of crashing.
     *
     * @param block The suspend lambda to execute.
     */
    protected fun launch(block: suspend CoroutineScope.() -> Unit) {
        val handler = CoroutineExceptionHandler { _, throwable -> onError(throwable) }
        viewModelScope.launch(handler, block = block)
    }

    /**
     * Called when an unhandled exception occurs in [launch].
     *
     * Override to show error UI or log the error.
     *
     * @param error The caught exception.
     */
    protected open fun onError(error: Throwable) {
        // Default: no-op. Subclasses override to handle errors.
    }
}
