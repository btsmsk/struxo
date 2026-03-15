package com.struxo.kit.core.data.network

import kotlinx.serialization.Serializable

/**
 * Generic wrapper for API JSON responses.
 *
 * Matches the standard backend envelope:
 * ```json
 * { "data": { ... }, "message": "OK", "success": true }
 * ```
 *
 * @param T The type of the [data] payload.
 */
@Serializable
data class ApiResponse<T>(
    val data: T,
    val message: String? = null,
    val success: Boolean = true,
)
