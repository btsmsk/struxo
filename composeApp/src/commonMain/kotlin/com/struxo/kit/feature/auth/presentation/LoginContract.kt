package com.struxo.kit.feature.auth.presentation

/**
 * MVI contract for the Login feature.
 */

/**
 * UI state for the login screen.
 *
 * @param email Current email input.
 * @param password Current password input.
 * @param isLoading Whether a login request is in progress.
 * @param emailError Inline validation error for the email field, or `null`.
 * @param passwordError Inline validation error for the password field, or `null`.
 */
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
)

/**
 * User actions on the login screen.
 */
sealed interface LoginEvent {
    /** Email text changed. */
    data class EmailChanged(val email: String) : LoginEvent

    /** Password text changed. */
    data class PasswordChanged(val password: String) : LoginEvent

    /** Login button tapped. */
    data object LoginClicked : LoginEvent

    /** Register link tapped. */
    data object RegisterClicked : LoginEvent

    /** Forgot password link tapped. */
    data object ForgotPasswordClicked : LoginEvent
}

/**
 * One-time side effects emitted by the login ViewModel.
 */
sealed interface LoginEffect {
    /** Navigate to the home screen after successful login. */
    data object NavigateToHome : LoginEffect

    /** Navigate to the registration screen. */
    data object NavigateToRegister : LoginEffect

    /** Navigate to the forgot password screen. */
    data object NavigateToForgotPassword : LoginEffect

    /** Show an error message (e.g. via Snackbar). */
    data class ShowError(val message: String) : LoginEffect
}
