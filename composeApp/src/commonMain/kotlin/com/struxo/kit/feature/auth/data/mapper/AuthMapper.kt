package com.struxo.kit.feature.auth.data.mapper

import com.struxo.kit.core.data.local.entity.AuthUserEntity
import com.struxo.kit.feature.auth.data.dto.LoginResponseDto
import com.struxo.kit.feature.auth.domain.model.AuthUser

/**
 * Maps [LoginResponseDto] to the domain [AuthUser].
 */
fun LoginResponseDto.toDomain(): AuthUser = AuthUser(
    id = id,
    email = email,
    name = name,
    avatarUrl = avatarUrl,
    accessToken = accessToken,
    refreshToken = refreshToken,
)

/**
 * Maps [AuthUser] to the Room [AuthUserEntity] for local caching.
 *
 * Tokens are **not** stored in the entity — they go to secure storage via [TokenProvider].
 */
fun AuthUser.toEntity(): AuthUserEntity = AuthUserEntity(
    id = id,
    email = email,
    name = name,
    avatarUrl = avatarUrl,
)

/**
 * Maps [AuthUserEntity] back to [AuthUser], attaching tokens from secure storage.
 *
 * @param accessToken Current access token from [TokenProvider].
 * @param refreshToken Current refresh token from [TokenProvider].
 */
fun AuthUserEntity.toDomain(accessToken: String, refreshToken: String): AuthUser = AuthUser(
    id = id,
    email = email,
    name = name,
    avatarUrl = avatarUrl,
    accessToken = accessToken,
    refreshToken = refreshToken,
)
