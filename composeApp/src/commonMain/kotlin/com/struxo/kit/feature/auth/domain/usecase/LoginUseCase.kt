package com.struxo.kit.feature.auth.domain.usecase

import com.struxo.kit.core.domain.usecase.UseCase
import com.struxo.kit.feature.auth.domain.model.AuthUser
import com.struxo.kit.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Validates credentials and delegates to [AuthRepository.login].
 *
 * Validation rules:
 * - Email must not be blank and must contain `@` with a domain part.
 * - Password must be at least 6 characters.
 *
 * @param repository Auth data source.
 * @param dispatcher Coroutine dispatcher (default: [Dispatchers.Default]).
 */
class LoginUseCase(
    private val repository: AuthRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : UseCase<LoginUseCase.Params, AuthUser>(dispatcher) {

    /**
     * @param email User's email address.
     * @param password User's password.
     */
    data class Params(val email: String, val password: String)

    override suspend fun execute(params: Params): AuthUser {
        val email = params.email.trim()
        val password = params.password

        require(email.isNotBlank()) { "Email is required" }
        require(isValidEmail(email)) { "Invalid email address" }
        require(password.length >= MIN_PASSWORD_LENGTH) {
            "Password must be at least $MIN_PASSWORD_LENGTH characters"
        }

        return repository.login(email, password)
    }

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6

        fun isValidEmail(email: String): Boolean =
            email.contains("@") && email.substringAfter("@").contains(".")
    }
}
