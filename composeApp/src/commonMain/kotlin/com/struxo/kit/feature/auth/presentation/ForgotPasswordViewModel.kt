package com.struxo.kit.feature.auth.presentation

import com.struxo.kit.core.presentation.base.BaseViewModel
import com.struxo.kit.core.util.Resource
import com.struxo.kit.feature.auth.domain.usecase.ForgotPasswordUseCase

/**
 * ViewModel for the forgot password screen.
 *
 * Validates email and sends a password reset request.
 *
 * @param forgotPasswordUseCase Validates email and triggers reset.
 */
class ForgotPasswordViewModel(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
) : BaseViewModel<ForgotPasswordState, ForgotPasswordEvent, ForgotPasswordEffect>(
    ForgotPasswordState(),
) {

    override fun onEvent(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.EmailChanged -> updateState {
                copy(email = event.email, emailError = null)
            }
            is ForgotPasswordEvent.SubmitClicked -> submit()
            is ForgotPasswordEvent.BackClicked -> sendEffect(ForgotPasswordEffect.NavigateBack)
        }
    }

    private fun submit() {
        val email = currentState.email.trim()

        // Client-side validation
        val emailError = when {
            email.isBlank() -> "Email is required"
            !email.contains("@") || !email.substringAfter("@").contains(".") ->
                "Invalid email address"
            else -> null
        }

        if (emailError != null) {
            updateState { copy(emailError = emailError) }
            return
        }

        launch {
            updateState { copy(isLoading = true, emailError = null) }

            when (val result = forgotPasswordUseCase(ForgotPasswordUseCase.Params(email))) {
                is Resource.Success -> {
                    updateState { copy(isLoading = false, isSuccess = true) }
                }
                is Resource.Error -> {
                    updateState { copy(isLoading = false) }
                    sendEffect(ForgotPasswordEffect.ShowError(result.message))
                }
                is Resource.Loading -> { /* Not emitted by UseCase.invoke() */ }
            }
        }
    }
}
