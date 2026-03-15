package com.struxo.kit.feature.auth.presentation

import com.struxo.kit.core.presentation.base.BaseViewModel
import com.struxo.kit.core.util.Resource
import com.struxo.kit.feature.auth.domain.usecase.LoginUseCase

/**
 * ViewModel for the login screen.
 *
 * Performs client-side validation before delegating to [LoginUseCase].
 *
 * @param loginUseCase Validates credentials and authenticates.
 */
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : BaseViewModel<LoginState, LoginEvent, LoginEffect>(LoginState()) {

    override fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> updateState {
                copy(email = event.email, emailError = null)
            }
            is LoginEvent.PasswordChanged -> updateState {
                copy(password = event.password, passwordError = null)
            }
            is LoginEvent.LoginClicked -> login()
            is LoginEvent.RegisterClicked -> sendEffect(LoginEffect.NavigateToRegister)
            is LoginEvent.ForgotPasswordClicked -> sendEffect(LoginEffect.NavigateToForgotPassword)
        }
    }

    private fun login() {
        val email = currentState.email.trim()
        val password = currentState.password

        // Client-side validation
        val emailError = when {
            email.isBlank() -> "Email is required"
            !email.contains("@") || !email.substringAfter("@").contains(".") ->
                "Invalid email address"
            else -> null
        }
        val passwordError = when {
            password.isBlank() -> "Password is required"
            password.length < MIN_PASSWORD_LENGTH ->
                "Password must be at least $MIN_PASSWORD_LENGTH characters"
            else -> null
        }

        if (emailError != null || passwordError != null) {
            updateState { copy(emailError = emailError, passwordError = passwordError) }
            return
        }

        launch {
            updateState { copy(isLoading = true, emailError = null, passwordError = null) }

            when (val result = loginUseCase(LoginUseCase.Params(email, password))) {
                is Resource.Success -> {
                    updateState { copy(isLoading = false) }
                    sendEffect(LoginEffect.NavigateToHome)
                }
                is Resource.Error -> {
                    updateState { copy(isLoading = false) }
                    sendEffect(LoginEffect.ShowError(result.message))
                }
                is Resource.Loading -> { /* Not emitted by UseCase.invoke() */ }
            }
        }
    }

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }
}
