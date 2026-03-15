package com.struxo.kit.feature.auth.presentation

/**
 * MVI contract for the Forgot Password feature.
 */

/**
 * UI state for the forgot password screen.
 *
 * @param email Current email input.
 * @param isLoading Whether a reset request is in progress.
 * @param emailError Inline validation error for the email field, or `null`.
 * @param isSuccess Whether the reset email was sent successfully.
 */
data class ForgotPasswordState(
    val email: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val isSuccess: Boolean = false,
)

/**
 * User actions on the forgot password screen.
 */
sealed interface ForgotPasswordEvent {
    /** Email text changed. */
    data class EmailChanged(val email: String) : ForgotPasswordEvent

    /** Submit button tapped. */
    data object SubmitClicked : ForgotPasswordEvent

    /** Back button tapped. */
    data object BackClicked : ForgotPasswordEvent
}

/**
 * One-time side effects emitted by the forgot password ViewModel.
 */
sealed interface ForgotPasswordEffect {
    /** Navigate back to the login screen. */
    data object NavigateBack : ForgotPasswordEffect

    /** Show an error message (e.g. via Snackbar). */
    data class ShowError(val message: String) : ForgotPasswordEffect
}
