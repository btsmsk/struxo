package com.struxo.kit.feature.auth.domain.usecase

import com.struxo.kit.core.domain.usecase.NoParamUseCase
import com.struxo.kit.feature.auth.domain.model.AuthUser
import com.struxo.kit.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Returns the currently authenticated user, or `null` if not logged in.
 *
 * @param repository Auth data source.
 * @param dispatcher Coroutine dispatcher (default: [Dispatchers.Default]).
 */
class GetCurrentUserUseCase(
    private val repository: AuthRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : NoParamUseCase<AuthUser?>(dispatcher) {

    override suspend fun execute(): AuthUser? {
        return repository.getCurrentUser()
    }
}
