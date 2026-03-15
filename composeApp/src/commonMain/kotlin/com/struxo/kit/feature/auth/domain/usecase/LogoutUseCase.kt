package com.struxo.kit.feature.auth.domain.usecase

import com.struxo.kit.core.domain.usecase.NoParamUseCase
import com.struxo.kit.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Signs out the current user, clearing tokens and cached data.
 *
 * @param repository Auth data source.
 * @param dispatcher Coroutine dispatcher (default: [Dispatchers.Default]).
 */
class LogoutUseCase(
    private val repository: AuthRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : NoParamUseCase<Unit>(dispatcher) {

    override suspend fun execute() {
        repository.logout()
    }
}
