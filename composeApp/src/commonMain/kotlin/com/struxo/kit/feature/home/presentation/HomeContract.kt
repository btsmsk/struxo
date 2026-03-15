package com.struxo.kit.feature.home.presentation

/**
 * MVI contract for the Home feature.
 */

/**
 * UI state for the home screen.
 *
 * @param userName Current user's display name.
 * @param userEmail Current user's email address.
 * @param isLoggingOut Whether a logout request is in progress.
 */
data class HomeState(
    val userName: String = "",
    val userEmail: String = "",
    val isLoggingOut: Boolean = false,
)

/**
 * User actions on the home screen.
 */
sealed interface HomeEvent {
    /** Logout button tapped. */
    data object LogoutClicked : HomeEvent
}

/**
 * One-time side effects emitted by the home ViewModel.
 */
sealed interface HomeEffect {
    /** Navigate to the login screen after logout. */
    data object NavigateToLogin : HomeEffect

    /** Show an error message (e.g. via Snackbar). */
    data class ShowError(val message: String) : HomeEffect
}
