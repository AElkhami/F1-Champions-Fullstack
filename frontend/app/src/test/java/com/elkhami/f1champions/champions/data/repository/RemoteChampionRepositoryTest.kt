package com.elkhami.f1champions.champions.data.repository


import com.elkhami.f1champions.champions.data.model.ChampionDto
import com.elkhami.f1champions.champions.data.remote.ChampionService
import com.elkhami.f1champions.core.network.AppError
import com.elkhami.f1champions.core.network.Result
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

/**
 * Created by A.Elkhami on 23/05/2025.
 */

class RemoteChampionRepositoryTest {

    private lateinit var service: ChampionService
    private lateinit var repository: RemoteChampionRepository

    @Before
    fun setUp() {
        service = mock()
        repository = RemoteChampionRepository(service)
    }

    @Test
    fun `getChampions returns success when API call is successful`() = runTest {
        val dtoList = listOf(
            ChampionDto("2020", "Lewis Hamilton", "Mercedes"),
            ChampionDto("2021", "Max Verstappen", "Red Bull")
        )
        `when`(service.getChampions()).thenReturn(dtoList)

        val result = repository.getChampions()

        assertTrue(result is Result.Success)
        val champions = (result as Result.Success).data
        assertEquals(2, champions.size)
        assertEquals("Lewis Hamilton", champions[0].driverName)
    }

    @Test
    fun `getChampions returns error when API throws exception`() = runTest {
        `when`(service.getChampions()).thenThrow(RuntimeException("Network error"))

        val result = repository.getChampions()

        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).error is AppError.Unknown) // or NetworkError based on your `safeCall`
    }
}
