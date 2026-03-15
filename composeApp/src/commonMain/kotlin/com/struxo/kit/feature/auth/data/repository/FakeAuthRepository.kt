package com.struxo.kit.feature.auth.data.repository

import com.struxo.kit.core.data.network.TokenProvider
import com.struxo.kit.feature.auth.data.local.AuthLocalSource
import com.struxo.kit.feature.auth.data.mapper.toDomain
import com.struxo.kit.feature.auth.data.mapper.toEntity
import com.struxo.kit.feature.auth.domain.model.AuthUser
import com.struxo.kit.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

/**
 * Fake [AuthRepository] that returns dummy data without a real backend.
 *
 * Persists user and tokens via [localSource] and [tokenProvider] so that
 * login state survives app restarts.
 *
 * Swap this in [com.struxo.kit.di.authModule] for demo/development.
 * Replace with [AuthRepositoryImpl] when a real API is available.
 *
 * @param localSource Local user cache.
 * @param tokenProvider Secure token storage.
 */
class FakeAuthRepository(
    private val localSource: AuthLocalSource,
    private val tokenProvider: TokenProvider,
) : AuthRepository {

    override suspend fun login(email: String, password: String): AuthUser {
        delay(1000) // simulate network latency
        val user = AuthUser(
            id = "demo-user-1",
            email = email,
            name = email.substringBefore("@"),
            avatarUrl = null,
            accessToken = "fake-access-token",
            refreshToken = "fake-refresh-token",
        )
        tokenProvider.saveTokens(user.accessToken, user.refreshToken)
        localSource.saveUser(user.toEntity())
        return user
    }

    override suspend fun register(email: String, password: String, name: String): AuthUser {
        delay(1000)
        val user = AuthUser(
            id = "demo-user-2",
            email = email,
            name = name,
            avatarUrl = null,
            accessToken = "fake-access-token",
            refreshToken = "fake-refresh-token",
        )
        tokenProvider.saveTokens(user.accessToken, user.refreshToken)
        localSource.saveUser(user.toEntity())
        return user
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

    override suspend fun resetPassword(email: String) {
        delay(1000) // simulate network latency
    }
}
