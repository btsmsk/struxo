package com.struxo.kit.core.data.network

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Configured Ktor [HttpClient] with content negotiation, auth, logging, and timeouts.
 *
 * All network requests in the app go through this client. Features use the typed
 * helper methods ([get], [post], [put], [patch], [delete]) which handle serialization
 * automatically.
 *
 * @param baseUrl Base URL for all requests (e.g. `"https://api.example.com/v1/"`).
 * @param engine Platform-specific HTTP engine (OkHttp on Android, Darwin on iOS).
 * @param tokenProvider Token storage for Bearer auth. Pass `null` to disable auth.
 */
class ApiClient(
    baseUrl: String,
    engine: HttpClientEngine,
    tokenProvider: TokenProvider? = null,
) {

    val httpClient: HttpClient = HttpClient(engine) {

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                    isLenient = true
                    encodeDefaults = true
                },
            )
        }

        if (tokenProvider != null) {
            install(Auth) {
                bearer {
                    loadTokens {
                        tokenProvider.getTokens()?.let { (access, refresh) ->
                            BearerTokens(access, refresh)
                        }
                    }
                    refreshTokens {
                        tokenProvider.refreshTokens()?.let { (access, refresh) ->
                            tokenProvider.saveTokens(access, refresh)
                            BearerTokens(access, refresh)
                        }
                    }
                }
            }
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.d(message, tag = "ApiClient")
                }
            }
            level = LogLevel.HEADERS
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 15_000
            socketTimeoutMillis = 15_000
        }

        defaultRequest {
            url(baseUrl)
            contentType(ContentType.Application.Json)
        }
    }

    /**
     * Performs a GET request and deserializes the response body.
     *
     * @param T Response type.
     * @param path URL path appended to the base URL.
     * @return Deserialized response body.
     */
    suspend inline fun <reified T> get(path: String): T =
        httpClient.get(path).body()

    /**
     * Performs a POST request with a serialized body.
     *
     * @param T Response type.
     * @param path URL path appended to the base URL.
     * @param body Request payload (serialized via ContentNegotiation).
     * @return Deserialized response body.
     */
    suspend inline fun <reified T> post(path: String, body: Any): T =
        httpClient.post(path) { setBody(body) }.body()

    /**
     * Performs a PUT request with a serialized body.
     *
     * @param T Response type.
     * @param path URL path appended to the base URL.
     * @param body Request payload.
     * @return Deserialized response body.
     */
    suspend inline fun <reified T> put(path: String, body: Any): T =
        httpClient.put(path) { setBody(body) }.body()

    /**
     * Performs a PATCH request with a serialized body.
     *
     * @param T Response type.
     * @param path URL path appended to the base URL.
     * @param body Request payload.
     * @return Deserialized response body.
     */
    suspend inline fun <reified T> patch(path: String, body: Any): T =
        httpClient.patch(path) { setBody(body) }.body()

    /**
     * Performs a DELETE request and deserializes the response body.
     *
     * @param T Response type.
     * @param path URL path appended to the base URL.
     * @return Deserialized response body.
     */
    suspend inline fun <reified T> delete(path: String): T =
        httpClient.delete(path).body()
}
