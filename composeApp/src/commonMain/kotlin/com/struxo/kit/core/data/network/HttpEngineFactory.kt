package com.struxo.kit.core.data.network

import io.ktor.client.engine.HttpClientEngine

/**
 * Creates the platform-specific [HttpClientEngine].
 *
 * - Android: OkHttp
 * - iOS: Darwin
 */
expect fun createHttpEngine(): HttpClientEngine
