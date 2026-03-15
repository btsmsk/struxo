package com.struxo.kit.feature.auth.presentation

import com.struxo.kit.core.presentation.base.BaseViewModel
import com.struxo.kit.core.util.Resource
import com.struxo.kit.feature.auth.domain.usecase.RegisterUseCase

/**
 * ViewModel for the registration screen.
 *
 * Performs client-side validation before delegating to [RegisterUseCase].
 *
 * @param registerUseCase Validates input and creates a new account.
 */
class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
) : BaseViewModel<RegisterState, RegisterEvent, RegisterEffect>(RegisterState()) {

    override fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.EmailChanged -> updateState {
                copy(email = event.email, emailError = null)
            }
            is RegisterEvent.PasswordChanged -> updateState {
                copy(password = event.password, passwordError = null)
            }
            is RegisterEvent.NameChanged -> updateState {
                copy(name = event.name, nameError = null)
            }
            is RegisterEvent.RegisterClicked -> register()
            is RegisterEvent.LoginClicked -> sendEffect(RegisterEffect.NavigateToLogin)
        }
    }

    private fun register() {
        val email = currentState.email.trim()
        val password = currentState.password
        val name = currentState.name.trim()

        // Client-side validation
        val nameError = when {
            name.isBlank() -> "Name is required"
            else -> null
        }
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

        if (nameError != null || emailError != null || passwordError != null) {
            updateState {
                copy(nameError = nameError, emailError = emailError, passwordError = passwordError)
            }
            return
        }

        launch {
            updateState {
                copy(isLoading = true, nameError = null, emailError = null, passwordError = null)
            }

            when (val result = registerUseCase(RegisterUseCase.Params(email, password, name))) {
                is Resource.Success -> {
                    updateState { copy(isLoading = false) }
                    sendEffect(RegisterEffect.NavigateToHome)
                }
                is Resource.Error -> {
                    updateState { copy(isLoading = false) }
                    sendEffect(RegisterEffect.ShowError(result.message))
                }
                is Resource.Loading -> { /* Not emitted by UseCase.invoke() */ }
            }
        }
    }

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }
}
