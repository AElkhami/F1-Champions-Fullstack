package com.elkhami.f1champions.seasondetails.presentation

import com.elkhami.f1champions.core.network.AppError
import com.elkhami.f1champions.core.network.Result
import com.elkhami.f1champions.seasondetails.domain.SeasonDetailsRepository
import com.elkhami.f1champions.seasondetails.domain.SeasonRaceResult
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

/**
 * Created by A.Elkhami on 23/05/2025.
 */

@OptIn(ExperimentalCoroutinesApi::class)
class SeasonDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: SeasonDetailsRepository
    private lateinit var viewModel: SeasonDetailsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        viewModel = SeasonDetailsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadSeason returns success and updates uiState`() = runTest {
        val mockResults = listOf(
            SeasonRaceResult("1", "Australian GP", "2025-03-10", "Hamilton", "Mercedes"),
            SeasonRaceResult("2", "Bahrain GP", "2025-03-24", "Verstappen", "Red Bull")
        )
        `when`(repository.getRaceResults("2025")).thenReturn(Result.Success(mockResults))

        viewModel.loadSeason("2025")
        advanceUntilIdle()

        val state = viewModel.uiState
        assertFalse(state.isLoading)
        assertEquals(2, state.races.size)
        assertEquals("Australian GP", state.races[0].raceName)
        assertEquals("Season 2025", state.seasonTitle)
    }

    @Test
    fun `loadSeason returns error and updates uiState`() = runTest {
        val error = AppError.Unknown
        `when`(repository.getRaceResults("2025")).thenReturn(Result.Error(error))

        viewModel.loadSeason("2025")
        advanceUntilIdle()

        val state = viewModel.uiState
        assertFalse(state.isLoading)
        assertTrue(state.races.isEmpty())
        assertEquals(error, state.error)
    }
}
