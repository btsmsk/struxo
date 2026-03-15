package com.struxo.kit.feature.auth.domain.usecase

import com.struxo.kit.core.util.Resource
import com.struxo.kit.feature.auth.FakeAuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class LoginUseCaseTest {

    private val repository = FakeAuthRepository()
    private val useCase = LoginUseCase(
        repository = repository,
        dispatcher = UnconfinedTestDispatcher(),
    )

    // region Success

    @Test
    fun `valid credentials return Success with AuthUser`() = runTest {
        val result = useCase(LoginUseCase.Params("test@example.com", "password123"))

        assertIs<Resource.Success<*>>(result)
        assertEquals(FakeAuthRepository.TEST_USER, result.data)
    }

    // endregion

    // region Validation errors

    @Test
    fun `empty email returns Error`() = runTest {
        val result = useCase(LoginUseCase.Params("", "password123"))

        assertIs<Resource.Error>(result)
        assertEquals("Email is required", result.message)
    }

    @Test
    fun `blank email returns Error`() = runTest {
        val result = useCase(LoginUseCase.Params("   ", "password123"))

        assertIs<Resource.Error>(result)
        assertEquals("Email is required", result.message)
    }

    @Test
    fun `invalid email format returns Error`() = runTest {
        val result = useCase(LoginUseCase.Params("notanemail", "password123"))

        assertIs<Resource.Error>(result)
        assertEquals("Invalid email address", result.message)
    }

    @Test
    fun `email without domain returns Error`() = runTest {
        val result = useCase(LoginUseCase.Params("user@", "password123"))

        assertIs<Resource.Error>(result)
        assertEquals("Invalid email address", result.message)
    }

    @Test
    fun `short password returns Error`() = runTest {
        val result = useCase(LoginUseCase.Params("test@example.com", "12345"))

        assertIs<Resource.Error>(result)
        assertEquals("Password must be at least 6 characters", result.message)
    }

    @Test
    fun `empty password returns Error`() = runTest {
        val result = useCase(LoginUseCase.Params("test@example.com", ""))

        assertIs<Resource.Error>(result)
        assertEquals("Password must be at least 6 characters", result.message)
    }

    // endregion

    // region Network error

    @Test
    fun `network error returns Error`() = runTest {
        repository.loginResult = Resource.Error("Network unavailable", Exception("timeout"))

        val result = useCase(LoginUseCase.Params("test@example.com", "password123"))

        assertIs<Resource.Error>(result)
        assertEquals("timeout", result.message)
    }

    // endregion
}
