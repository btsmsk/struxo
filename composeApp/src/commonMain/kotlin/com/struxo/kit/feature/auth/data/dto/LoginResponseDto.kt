package com.struxo.kit.feature.auth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response body from the `POST /auth/login` endpoint.
 */
@Serializable
data class LoginResponseDto(
    val id: String,
    val email: String,
    val name: String,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
)
