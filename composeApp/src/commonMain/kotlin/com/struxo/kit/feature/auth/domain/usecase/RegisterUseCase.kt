package com.struxo.kit.feature.auth.domain.usecase

import com.struxo.kit.core.domain.usecase.UseCase
import com.struxo.kit.feature.auth.domain.model.AuthUser
import com.struxo.kit.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Validates input and delegates to [AuthRepository.register].
 *
 * Validation rules:
 * - Email must not be blank and must contain `@` with a domain part.
 * - Password must be at least 6 characters.
 * - Name must not be blank.
 *
 * @param repository Auth data source.
 * @param dispatcher Coroutine dispatcher (default: [Dispatchers.Default]).
 */
class RegisterUseCase(
    private val repository: AuthRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : UseCase<RegisterUseCase.Params, AuthUser>(dispatcher) {

    /**
     * @param email User's email address.
     * @param password User's password.
     * @param name User's display name.
     */
    data class Params(val email: String, val password: String, val name: String)

    override suspend fun execute(params: Params): AuthUser {
        val email = params.email.trim()
        val password = params.password
        val name = params.name.trim()

        require(name.isNotBlank()) { "Name is required" }
        require(email.isNotBlank()) { "Email is required" }
        require(isValidEmail(email)) { "Invalid email address" }
        require(password.length >= MIN_PASSWORD_LENGTH) {
            "Password must be at least $MIN_PASSWORD_LENGTH characters"
        }

        return repository.register(email, password, name)
    }

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6

        fun isValidEmail(email: String): Boolean =
            email.contains("@") && email.substringAfter("@").contains(".")
    }
}
