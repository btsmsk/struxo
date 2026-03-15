package com.struxo.kit.feature.auth.data.dto

import kotlinx.serialization.Serializable

/**
 * Request body for the `POST /auth/login` endpoint.
 */
@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String,
)
