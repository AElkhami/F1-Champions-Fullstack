package com.elkhami.f1champions.core.network

import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * Created by A.Elkhami on 23/05/2025.
 */


class SafeCallTest {

    @Test
    fun `safeCall returns Success when block succeeds`() = runTest {
        val result = safeCall { "expectedResult" }
        assertTrue(result is Result.Success)
        assertEquals("expectedResult", (result as Result.Success).data)
    }

    @Test
    fun `safeCall returns Error Http when HttpException is thrown`() = runTest {
        val httpException = HttpException(Response.error<String>(404,
            "Not found".toResponseBody(null)
        ))

        val result = safeCall {
            throw httpException
        }

        assertTrue(result is Result.Error)
        assertEquals(AppError.Http(404), (result as Result.Error).error)
    }

    @Test
    fun `safeCall returns Error Network when IOException is thrown`() = runTest {
        val result = safeCall {
            throw IOException("Network issue")
        }

        assertTrue(result is Result.Error)
        assertEquals(AppError.Network, (result as Result.Error).error)
    }

    @Test
    fun `safeCall returns Error Unknown when generic Exception is thrown`() = runTest {
        val result = safeCall {
            throw IllegalStateException("Something went wrong")
        }

        assertTrue(result is Result.Error)
        assertEquals(AppError.Unknown, (result as Result.Error).error)
    }
}
