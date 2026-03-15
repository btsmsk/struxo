package com.struxo.kit.core.domain.usecase

import com.struxo.kit.core.util.Resource
import com.struxo.kit.core.util.safeCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

/**
 * Base use case for suspend operations that accept parameters.
 *
 * Subclasses implement [execute] with domain logic; invocation automatically
 * wraps the result in [Resource] and catches exceptions.
 *
 * @param P Parameter type.
 * @param R Result type.
 * @param dispatcher Coroutine dispatcher for execution (default: [Dispatchers.Default]).
 */
abstract class UseCase<in P, R>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    /**
     * Executes the use case, returning a [Resource] wrapper.
     *
     * @param params Input parameters.
     * @return [Resource.Success] with data or [Resource.Error] on failure.
     */
    suspend operator fun invoke(params: P): Resource<R> = withContext(dispatcher) {
        safeCall { execute(params) }
    }

    /**
     * Domain logic to be implemented by subclasses.
     *
     * @param params Input parameters.
     * @return The result data.
     * @throws Exception on failure (caught and wrapped by [invoke]).
     */
    protected abstract suspend fun execute(params: P): R
}

/**
 * Base use case for suspend operations that require no parameters.
 *
 * @param R Result type.
 * @param dispatcher Coroutine dispatcher for execution (default: [Dispatchers.Default]).
 */
abstract class NoParamUseCase<R>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    /**
     * Executes the use case, returning a [Resource] wrapper.
     *
     * @return [Resource.Success] with data or [Resource.Error] on failure.
     */
    suspend operator fun invoke(): Resource<R> = withContext(dispatcher) {
        safeCall { execute() }
    }

    /**
     * Domain logic to be implemented by subclasses.
     *
     * @return The result data.
     * @throws Exception on failure (caught and wrapped by [invoke]).
     */
    protected abstract suspend fun execute(): R
}

/**
 * Base use case for operations that return a [Flow] of results.
 *
 * The returned flow emits [Resource.Loading] first, then maps each item
 * to [Resource.Success], catching errors as [Resource.Error].
 *
 * @param P Parameter type.
 * @param R Result type.
 * @param dispatcher Coroutine dispatcher for flow collection (default: [Dispatchers.Default]).
 */
abstract class FlowUseCase<in P, R>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    /**
     * Executes the use case, returning a [Flow] of [Resource] wrappers.
     *
     * @param params Input parameters.
     * @return Flow that emits Loading, then Success items, or Error on failure.
     */
    operator fun invoke(params: P): Flow<Resource<R>> = execute(params)
        .map<R, Resource<R>> { Resource.Success(it) }
        .onStart { emit(Resource.Loading) }
        .catch { e -> emit(Resource.Error(e.message ?: "Unknown error", e)) }
        .flowOn(dispatcher)

    /**
     * Domain logic to be implemented by subclasses.
     *
     * @param params Input parameters.
     * @return A [Flow] emitting result items.
     */
    protected abstract fun execute(params: P): Flow<R>
}
