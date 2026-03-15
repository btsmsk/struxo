package com.struxo.kit.feature.auth.domain.model

/**
 * Authenticated user domain entity.
 *
 * Tokens are included so the presentation layer can trigger token-aware
 * actions without depending on the data layer.
 *
 * @param id Unique user identifier.
 * @param email User's email address.
 * @param name Display name.
 * @param avatarUrl Profile image URL, or `null`.
 * @param accessToken JWT access token.
 * @param refreshToken JWT refresh token.
 */
data class AuthUser(
    val id: String,
    val email: String,
    val name: String,
    val avatarUrl: String?,
    val accessToken: String,
    val refreshToken: String,
)
