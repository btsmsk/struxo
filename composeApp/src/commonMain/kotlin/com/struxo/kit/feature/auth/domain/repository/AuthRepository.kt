package com.struxo.kit.feature.auth.domain.repository

import com.struxo.kit.feature.auth.domain.model.AuthUser

/**
 * Contract for authentication operations.
 *
 * Implemented by [com.struxo.kit.feature.auth.data.repository.AuthRepositoryImpl]
 * which coordinates remote API and local cache.
 */
interface AuthRepository {

    /**
     * Authenticates a user with email and password.
     *
     * @param email User's email address.
     * @param password User's password.
     * @return The authenticated [AuthUser].
     * @throws Exception on network or server error.
     */
    suspend fun login(email: String, password: String): AuthUser

    /**
     * Registers a new user account.
     *
     * @param email User's email address.
     * @param password User's password.
     * @param name User's display name.
     * @return The newly created [AuthUser].
     * @throws Exception on network or server error.
     */
    suspend fun register(email: String, password: String, name: String): AuthUser

    /**
     * Signs out the current user, clearing tokens and cached data.
     */
    suspend fun logout()

    /**
     * Returns the currently authenticated user from local cache, or `null` if not logged in.
     */
    suspend fun getCurrentUser(): AuthUser?

    /**
     * Checks whether a user is currently authenticated (tokens exist).
     */
    suspend fun isLoggedIn(): Boolean
}
