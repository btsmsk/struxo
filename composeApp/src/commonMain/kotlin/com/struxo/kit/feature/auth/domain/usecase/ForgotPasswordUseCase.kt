package com.struxo.kit.feature.auth.domain.usecase

import com.struxo.kit.core.domain.usecase.UseCase
import com.struxo.kit.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Validates email and delegates to [AuthRepository.resetPassword].
 *
 * @param repository Auth data source.
 * @param dispatcher Coroutine dispatcher (default: [Dispatchers.Default]).
 */
class ForgotPasswordUseCase(
    private val repository: AuthRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : UseCase<ForgotPasswordUseCase.Params, Unit>(dispatcher) {

    /**
     * @param email User's email address for password reset.
     */
    data class Params(val email: String)

    override suspend fun execute(params: Params) {
        val email = params.email.trim()

        require(email.isNotBlank()) { "Email is required" }
        require(isValidEmail(email)) { "Invalid email address" }

        repository.resetPassword(email)
    }

    private companion object {
        fun isValidEmail(email: String): Boolean =
            email.contains("@") && email.substringAfter("@").contains(".")
    }
}
