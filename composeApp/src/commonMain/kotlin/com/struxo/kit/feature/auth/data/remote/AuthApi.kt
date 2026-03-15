package com.struxo.kit.feature.auth.data.remote

import com.struxo.kit.core.data.network.ApiClient
import com.struxo.kit.core.data.network.ApiResponse
import com.struxo.kit.feature.auth.data.dto.LoginRequestDto
import com.struxo.kit.feature.auth.data.dto.LoginResponseDto

/**
 * Remote API endpoints for authentication.
 *
 * @param apiClient Configured [ApiClient] instance.
 */
class AuthApi(private val apiClient: ApiClient) {

    /**
     * Authenticates a user.
     *
     * @param request Login credentials.
     * @return The authenticated user data from the server.
     */
    suspend fun login(request: LoginRequestDto): LoginResponseDto =
        apiClient.post<ApiResponse<LoginResponseDto>>("auth/login", request).data
}
