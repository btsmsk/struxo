package com.struxo.kit.core.util

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ResourceTest {

    // region Extension properties

    @Test
    fun `Loading isLoading returns true`() {
        val resource: Resource<String> = Resource.Loading
        assertTrue(resource.isLoading)
        assertFalse(resource.isSuccess)
        assertFalse(resource.isError)
    }

    @Test
    fun `Success isSuccess returns true`() {
        val resource: Resource<String> = Resource.Success("data")
        assertTrue(resource.isSuccess)
        assertFalse(resource.isLoading)
        assertFalse(resource.isError)
    }

    @Test
    fun `Error isError returns true`() {
        val resource: Resource<String> = Resource.Error("fail")
        assertTrue(resource.isError)
        assertFalse(resource.isLoading)
        assertFalse(resource.isSuccess)
    }

    // endregion

    // region getOrNull / errorOrNull

    @Test
    fun `getOrNull returns data for Success`() {
        val resource = Resource.Success(42)
        assertEquals(42, resource.getOrNull())
    }

    @Test
    fun `getOrNull returns null for Loading`() {
        val resource: Resource<Int> = Resource.Loading
        assertNull(resource.getOrNull())
    }

    @Test
    fun `getOrNull returns null for Error`() {
        val resource: Resource<Int> = Resource.Error("fail")
        assertNull(resource.getOrNull())
    }

    @Test
    fun `errorOrNull returns message for Error`() {
        val resource: Resource<Int> = Resource.Error("something went wrong")
        assertEquals("something went wrong", resource.errorOrNull())
    }

    @Test
    fun `errorOrNull returns null for Success`() {
        val resource = Resource.Success("ok")
        assertNull(resource.errorOrNull())
    }

    @Test
    fun `errorOrNull returns null for Loading`() {
        val resource: Resource<Int> = Resource.Loading
        assertNull(resource.errorOrNull())
    }

    // endregion

    // region map

    @Test
    fun `map transforms Success data`() {
        val resource = Resource.Success(10)
        val mapped = resource.map { it * 2 }
        assertIs<Resource.Success<Int>>(mapped)
        assertEquals(20, mapped.data)
    }

    @Test
    fun `map preserves Loading`() {
        val resource: Resource<Int> = Resource.Loading
        val mapped = resource.map { it * 2 }
        assertIs<Resource.Loading>(mapped)
    }

    @Test
    fun `map preserves Error`() {
        val resource: Resource<Int> = Resource.Error("fail", RuntimeException("cause"))
        val mapped = resource.map { it * 2 }
        assertIs<Resource.Error>(mapped)
        assertEquals("fail", mapped.message)
        assertEquals("cause", mapped.throwable?.message)
    }

    // endregion

    // region safeCall

    @Test
    fun `safeCall returns Success on success`() = runTest {
        val result = safeCall { "hello" }
        assertIs<Resource.Success<String>>(result)
        assertEquals("hello", result.data)
    }

    @Test
    fun `safeCall returns Error on exception`() = runTest {
        val result = safeCall<String> { throw IllegalStateException("boom") }
        assertIs<Resource.Error>(result)
        assertEquals("boom", result.message)
        assertIs<IllegalStateException>(result.throwable)
    }

    @Test
    fun `safeCall uses Unknown error for null message`() = runTest {
        val result = safeCall<String> { throw object : Exception(null as String?) {} }
        assertIs<Resource.Error>(result)
        assertEquals("Unknown error", result.message)
    }

    // endregion

    // region toResource

    @Test
    fun `Result success converts to Resource Success`() {
        val result = Result.success(99)
        val resource = result.toResource()
        assertIs<Resource.Success<Int>>(resource)
        assertEquals(99, resource.data)
    }

    @Test
    fun `Result failure converts to Resource Error`() {
        val exception = RuntimeException("failed")
        val result = Result.failure<Int>(exception)
        val resource = result.toResource()
        assertIs<Resource.Error>(resource)
        assertEquals("failed", resource.message)
        assertEquals(exception, resource.throwable)
    }

    // endregion
}
