package com.struxo.kit.feature.auth.presentation

import app.cash.turbine.test
import com.struxo.kit.core.util.Resource
import com.struxo.kit.feature.auth.FakeAuthRepository
import com.struxo.kit.feature.auth.domain.usecase.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var repository: FakeAuthRepository
    private lateinit var viewModel: LoginViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = FakeAuthRepository()
        val loginUseCase = LoginUseCase(
            repository = repository,
            dispatcher = UnconfinedTestDispatcher(),
        )
        viewModel = LoginViewModel(loginUseCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // region Initial state

    @Test
    fun `initial state has empty fields and no errors`() = runTest {
        val state = viewModel.currentState
        assertEquals("", state.email)
        assertEquals("", state.password)
        assertFalse(state.isLoading)
        assertNull(state.emailError)
        assertNull(state.passwordError)
    }

    // endregion

    // region State changes

    @Test
    fun `EmailChanged updates email and clears error`() = runTest {
        viewModel.uiState.test {
            awaitItem() // initial state

            viewModel.onEvent(LoginEvent.EmailChanged("user@test.com"))
            val updated = awaitItem()
            assertEquals("user@test.com", updated.email)
            assertNull(updated.emailError)
        }
    }

    @Test
    fun `PasswordChanged updates password and clears error`() = runTest {
        viewModel.uiState.test {
            awaitItem() // initial state

            viewModel.onEvent(LoginEvent.PasswordChanged("secret"))
            val updated = awaitItem()
            assertEquals("secret", updated.password)
            assertNull(updated.passwordError)
        }
    }

    // endregion

    // region Client-side validation

    @Test
    fun `LoginClicked with empty email shows email error`() = runTest {
        viewModel.onEvent(LoginEvent.PasswordChanged("password123"))
        viewModel.onEvent(LoginEvent.LoginClicked)

        val state = viewModel.currentState
        assertEquals("Email is required", state.emailError)
        assertNull(state.passwordError)
    }

    @Test
    fun `LoginClicked with invalid email shows email error`() = runTest {
        viewModel.onEvent(LoginEvent.EmailChanged("notanemail"))
        viewModel.onEvent(LoginEvent.PasswordChanged("password123"))
        viewModel.onEvent(LoginEvent.LoginClicked)

        val state = viewModel.currentState
        assertEquals("Invalid email address", state.emailError)
    }

    @Test
    fun `LoginClicked with short password shows password error`() = runTest {
        viewModel.onEvent(LoginEvent.EmailChanged("user@test.com"))
        viewModel.onEvent(LoginEvent.PasswordChanged("12345"))
        viewModel.onEvent(LoginEvent.LoginClicked)

        val state = viewModel.currentState
        assertNull(state.emailError)
        assertEquals("Password must be at least 6 characters", state.passwordError)
    }

    // endregion

    // region Successful login

    @Test
    fun `successful login emits NavigateToHome`() = runTest {
        viewModel.effect.test {
            viewModel.onEvent(LoginEvent.EmailChanged("test@example.com"))
            viewModel.onEvent(LoginEvent.PasswordChanged("password123"))
            viewModel.onEvent(LoginEvent.LoginClicked)

            assertIs<LoginEffect.NavigateToHome>(awaitItem())
        }
        assertFalse(viewModel.currentState.isLoading)
    }

    // endregion

    // region Failed login

    @Test
    fun `network error emits ShowError`() = runTest {
        repository.loginResult = Resource.Error("Connection failed", Exception("Connection failed"))

        viewModel.effect.test {
            viewModel.onEvent(LoginEvent.EmailChanged("test@example.com"))
            viewModel.onEvent(LoginEvent.PasswordChanged("password123"))
            viewModel.onEvent(LoginEvent.LoginClicked)

            val effect = awaitItem()
            assertIs<LoginEffect.ShowError>(effect)
            assertEquals("Connection failed", effect.message)
        }
        assertFalse(viewModel.currentState.isLoading)
    }

    // endregion

    // region Navigation effects

    @Test
    fun `RegisterClicked emits NavigateToRegister`() = runTest {
        viewModel.effect.test {
            viewModel.onEvent(LoginEvent.RegisterClicked)
            assertIs<LoginEffect.NavigateToRegister>(awaitItem())
        }
    }

    // endregion
}
