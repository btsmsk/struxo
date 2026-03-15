package com.struxo.kit.feature.auth

import com.struxo.kit.core.util.Resource
import com.struxo.kit.feature.auth.domain.model.AuthUser
import com.struxo.kit.feature.auth.domain.repository.AuthRepository

/**
 * Controllable fake for [AuthRepository] used in unit tests.
 *
 * Set [loginResult] before calling [login] to control the outcome.
 */
class FakeAuthRepository : AuthRepository {

    /** Pre-configured result returned by [login]. */
    var loginResult: Resource<AuthUser> = Resource.Success(TEST_USER)

    /** Tracks whether [logout] was called. */
    var loggedOut: Boolean = false
        private set

    private var currentUser: AuthUser? = null

    override suspend fun login(email: String, password: String): AuthUser =
        when (val result = loginResult) {
            is Resource.Success -> {
                currentUser = result.data
                result.data
            }
            is Resource.Error -> throw (result.throwable ?: Exception(result.message))
            is Resource.Loading -> error("Unexpected Loading state in FakeAuthRepository")
        }

    override suspend fun register(email: String, password: String, name: String): AuthUser =
        error("Not implemented in fake")

    override suspend fun logout() {
        loggedOut = true
        currentUser = null
    }

    override suspend fun getCurrentUser(): AuthUser? = currentUser

    override suspend fun isLoggedIn(): Boolean = currentUser != null

    companion object {
        val TEST_USER = AuthUser(
            id = "user-1",
            email = "test@example.com",
            name = "Test User",
            avatarUrl = null,
            accessToken = "access-token",
            refreshToken = "refresh-token",
        )
    }
}
