package com.struxo.kit.feature.auth.presentation

/**
 * MVI contract for the Register feature.
 */

/**
 * UI state for the registration screen.
 *
 * @param email Current email input.
 * @param password Current password input.
 * @param name Current name input.
 * @param isLoading Whether a registration request is in progress.
 * @param emailError Inline validation error for the email field, or `null`.
 * @param passwordError Inline validation error for the password field, or `null`.
 * @param nameError Inline validation error for the name field, or `null`.
 */
data class RegisterState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val nameError: String? = null,
)

/**
 * User actions on the registration screen.
 */
sealed interface RegisterEvent {
    /** Email text changed. */
    data class EmailChanged(val email: String) : RegisterEvent

    /** Password text changed. */
    data class PasswordChanged(val password: String) : RegisterEvent

    /** Name text changed. */
    data class NameChanged(val name: String) : RegisterEvent

    /** Register button tapped. */
    data object RegisterClicked : RegisterEvent

    /** Sign in link tapped. */
    data object LoginClicked : RegisterEvent
}

/**
 * One-time side effects emitted by the register ViewModel.
 */
sealed interface RegisterEffect {
    /** Navigate to the home screen after successful registration. */
    data object NavigateToHome : RegisterEffect

    /** Navigate back to the login screen. */
    data object NavigateToLogin : RegisterEffect

    /** Show an error message (e.g. via Snackbar). */
    data class ShowError(val message: String) : RegisterEffect
}
