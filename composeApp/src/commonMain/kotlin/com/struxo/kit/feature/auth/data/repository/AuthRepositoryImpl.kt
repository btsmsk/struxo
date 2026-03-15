package com.struxo.kit.feature.auth.data.repository

import com.struxo.kit.core.data.network.TokenProvider
import com.struxo.kit.feature.auth.data.dto.LoginRequestDto
import com.struxo.kit.feature.auth.data.local.AuthLocalSource
import com.struxo.kit.feature.auth.data.mapper.toDomain
import com.struxo.kit.feature.auth.data.mapper.toEntity
import com.struxo.kit.feature.auth.data.remote.AuthApi
import com.struxo.kit.feature.auth.domain.model.AuthUser
import com.struxo.kit.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first

/**
 * [AuthRepository] implementation coordinating remote API and local cache.
 *
 * @param authApi Remote auth endpoints.
 * @param localSource Local user cache.
 * @param tokenProvider Secure token storage.
 */
class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val localSource: AuthLocalSource,
    private val tokenProvider: TokenProvider,
) : AuthRepository {

    override suspend fun login(email: String, password: String): AuthUser {
        val response = authApi.login(LoginRequestDto(email, password))
        val user = response.toDomain()
        tokenProvider.saveTokens(user.accessToken, user.refreshToken)
        localSource.saveUser(user.toEntity())
        return user
    }

    override suspend fun register(email: String, password: String, name: String): AuthUser {
        TODO("Register endpoint will be added when the backend provides it")
    }

    override suspend fun logout() {
        tokenProvider.clearTokens()
        localSource.clearAll()
    }

    override suspend fun getCurrentUser(): AuthUser? {
        val tokens = tokenProvider.getTokens() ?: return null
        val entity = localSource.observeAll().first().firstOrNull() ?: return null
        return entity.toDomain(accessToken = tokens.first, refreshToken = tokens.second)
    }

    override suspend fun isLoggedIn(): Boolean =
        tokenProvider.getTokens() != null
}
