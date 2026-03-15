package com.struxo.kit.core.data.network

/**
 * Abstraction for auth token storage and refresh.
 *
 * Platform implementations handle secure storage
 * (EncryptedSharedPreferences on Android, Keychain on iOS).
 */
interface TokenProvider {

    /**
     * Returns the current access and refresh tokens, or `null` if not authenticated.
     *
     * @return Pair of (accessToken, refreshToken) or `null`.
     */
    suspend fun getTokens(): Pair<String, String>?

    /**
     * Attempts to refresh the tokens using the current refresh token.
     *
     * @return New (accessToken, refreshToken) pair or `null` if refresh failed.
     */
    suspend fun refreshTokens(): Pair<String, String>?

    /**
     * Persists new tokens after a successful login or refresh.
     *
     * @param access The new access token.
     * @param refresh The new refresh token.
     */
    suspend fun saveTokens(access: String, refresh: String)

    /**
     * Clears all stored tokens (logout).
     */
    suspend fun clearTokens()
}
