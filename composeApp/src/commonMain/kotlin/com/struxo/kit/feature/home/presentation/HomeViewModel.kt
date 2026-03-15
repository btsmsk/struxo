package com.struxo.kit.feature.home.presentation

import com.struxo.kit.core.presentation.base.BaseViewModel
import com.struxo.kit.core.util.Resource
import com.struxo.kit.feature.auth.domain.usecase.GetCurrentUserUseCase
import com.struxo.kit.feature.auth.domain.usecase.LogoutUseCase

/**
 * ViewModel for the home screen.
 *
 * Loads the current user on init and handles logout.
 *
 * @param logoutUseCase Signs out the user.
 * @param getCurrentUserUseCase Retrieves the authenticated user.
 */
class HomeViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>(HomeState()) {

    init {
        loadCurrentUser()
    }

    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LogoutClicked -> logout()
        }
    }

    private fun loadCurrentUser() {
        launch {
            when (val result = getCurrentUserUseCase()) {
                is Resource.Success -> {
                    val user = result.data
                    if (user != null) {
                        updateState { copy(userName = user.name, userEmail = user.email) }
                    }
                }
                is Resource.Error -> {
                    sendEffect(HomeEffect.ShowError(result.message))
                }
                is Resource.Loading -> { /* Not emitted by NoParamUseCase.invoke() */ }
            }
        }
    }

    private fun logout() {
        launch {
            updateState { copy(isLoggingOut = true) }

            when (val result = logoutUseCase()) {
                is Resource.Success -> {
                    updateState { copy(isLoggingOut = false) }
                    sendEffect(HomeEffect.NavigateToLogin)
                }
                is Resource.Error -> {
                    updateState { copy(isLoggingOut = false) }
                    sendEffect(HomeEffect.ShowError(result.message))
                }
                is Resource.Loading -> { /* Not emitted by NoParamUseCase.invoke() */ }
            }
        }
    }
}
