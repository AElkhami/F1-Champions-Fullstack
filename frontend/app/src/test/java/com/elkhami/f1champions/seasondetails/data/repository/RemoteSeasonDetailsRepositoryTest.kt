package com.elkhami.f1champions.seasondetails.data.repository

import com.elkhami.f1champions.core.network.AppError
import com.elkhami.f1champions.core.network.Result
import com.elkhami.f1champions.seasondetails.data.model.SeasonDetailsDto
import com.elkhami.f1champions.seasondetails.data.remote.SeasonDetailsService
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * Created by A.Elkhami on 23/05/2025.
 */

class RemoteSeasonDetailsRepositoryTest {

    private lateinit var service: SeasonDetailsService
    private lateinit var repository: RemoteSeasonDetailsRepository

    @Before
    fun setUp() {
        service = mock()
        repository = RemoteSeasonDetailsRepository(service)
    }

    @Test
    fun `getRaceResults returns success when API returns data`() = runTest {
        val dtoList = listOf(
            SeasonDetailsDto("1", "Bahrain GP", "2025-03-10", "hamilton", "Mercedes"),
            SeasonDetailsDto("2", "Jeddah GP", "2025-03-24", "max", "Red Bull")
        )
        `when`(service.getRaceResults("2025")).thenReturn(dtoList)

        val result = repository.getRaceResults("2025")

        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals(2, data.size)
        assertEquals("Bahrain GP", data[0].raceName)
    }

    @Test
    fun `getRaceResults returns error when API throws HttpException`() = runTest {
        val httpException = HttpException(Response.error<String>(404, "".toResponseBody(null)))
        `when`(service.getRaceResults("2025")).thenThrow(httpException)

        val result = repository.getRaceResults("2025")

        assertTrue(result is Result.Error)
        assertEquals(AppError.Http(404), (result as Result.Error).error)
    }

    @Test
    fun `getRaceResults returns error when API throws IOException`() = runTest {
        whenever(service.getRaceResults("2025")).thenAnswer {
            throw IOException("Network error")
        }

        val result = repository.getRaceResults("2025")

        assertTrue(result is Result.Error)
        assertEquals(AppError.Network, (result as Result.Error).error)
    }

    @Test
    fun `getRaceResults returns error when API throws unknown exception`() = runTest {
        `when`(service.getRaceResults("2025")).thenThrow(IllegalStateException("Something went wrong"))

        val result = repository.getRaceResults("2025")

        assertTrue(result is Result.Error)
        assertEquals(AppError.Unknown, (result as Result.Error).error)
    }
}
