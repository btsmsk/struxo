package com.struxo.kit.core.util

import kotlin.coroutines.cancellation.CancellationException

/**
 * Wrapper for data operation results across all layers.
 *
 * Every repository method, UseCase, and ViewModel state update uses [Resource]
 * to represent loading, success, or error states uniformly.
 */
sealed class Resource<out T> {

    /** Operation is in progress. */
    data object Loading : Resource<Nothing>()

    /**
     * Operation completed successfully.
     *
     * @param data The result payload.
     */
    data class Success<T>(val data: T) : Resource<T>()

    /**
     * Operation failed.
     *
     * @param message Human-readable error description.
     * @param throwable Optional underlying exception.
     */
    data class Error(
        val message: String,
        val throwable: Throwable? = null,
    ) : Resource<Nothing>()
}

/** `true` when [Resource.Loading]. */
val Resource<*>.isLoading: Boolean get() = this is Resource.Loading

/** `true` when [Resource.Success]. */
val Resource<*>.isSuccess: Boolean get() = this is Resource.Success

/** `true` when [Resource.Error]. */
val Resource<*>.isError: Boolean get() = this is Resource.Error

/**
 * Returns the success [data][Resource.Success.data] or `null`.
 */
fun <T> Resource<T>.getOrNull(): T? = (this as? Resource.Success)?.data

/**
 * Returns the [error message][Resource.Error.message] or `null`.
 */
fun Resource<*>.errorOrNull(): String? = (this as? Resource.Error)?.message

/**
 * Transforms the success data while preserving Loading/Error states.
 *
 * @param transform Mapping function applied to [Resource.Success.data].
 * @return A new [Resource] with the transformed data type.
 */
fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> = when (this) {
    is Resource.Loading -> Resource.Loading
    is Resource.Success -> Resource.Success(transform(data))
    is Resource.Error -> Resource.Error(message, throwable)
}

/**
 * Executes [block] and wraps the result in [Resource.Success],
 * catching any exception (except [CancellationException]) as [Resource.Error].
 *
 * @param block Suspend lambda producing the result.
 * @return [Resource.Success] on success, [Resource.Error] on failure.
 */
suspend fun <T> safeCall(block: suspend () -> T): Resource<T> = try {
    Resource.Success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    Resource.Error(e.message ?: "Unknown error", e)
}

/**
 * Converts a [kotlin.Result] to a [Resource].
 *
 * @return [Resource.Success] if the result is successful, [Resource.Error] otherwise.
 */
fun <T> Result<T>.toResource(): Resource<T> = fold(
    onSuccess = { Resource.Success(it) },
    onFailure = { Resource.Error(it.message ?: "Unknown error", it) },
)
