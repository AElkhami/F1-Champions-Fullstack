package com.elkhami.f1champions.champions.presentation

import com.elkhami.f1champions.champions.domain.Champion
import com.elkhami.f1champions.champions.domain.ChampionRepository
import com.elkhami.f1champions.core.network.AppError
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import com.elkhami.f1champions.core.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After

/**
 * Created by A.Elkhami on 23/05/2025.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ChampionsViewModelTest{

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ChampionsViewModel
    private lateinit var repository: ChampionRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        viewModel = ChampionsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `loadChampions returns success and updates uiState`() = runTest {
        val mockData = listOf(
            Champion("2020", "Lewis Hamilton", "Mercedes"),
            Champion("2021", "Max Verstappen", "Red Bull")
        )
        `when`(repository.getChampions()).thenReturn(Result.Success(mockData))

        viewModel.loadChampions()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.isLoading)
        assertEquals(2, viewModel.uiState.champions.size)
        assertEquals("2020", viewModel.uiState.champions[0].season)
        assertEquals("Max Verstappen", viewModel.uiState.champions[1].driver)
    }

    @Test
    fun `loadChampions returns error and updates uiState`() = runTest {
        val error = AppError.Unknown
        `when`(repository.getChampions()).thenReturn(Result.Error(error))

        viewModel.loadChampions()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.isLoading)
        assertEquals(error, viewModel.uiState.error)
        assertTrue(viewModel.uiState.champions.isEmpty())
    }

    @Test
    fun `refreshChampions returns success and updates uiState`() = runTest {
        val mockData = listOf(
            Champion("2022", "Charles Leclerc", "Ferrari")
        )
        `when`(repository.getChampions()).thenReturn(Result.Success(mockData))

        viewModel.refreshChampions()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.isRefreshing)
        assertEquals(1, viewModel.uiState.champions.size)
        assertEquals("Ferrari", viewModel.uiState.champions[0].constructor)
        assertEquals(null, viewModel.uiState.error)
    }

    @Test
    fun `refreshChampions returns error and updates uiState`() = runTest {
        val error = AppError.Unknown
        `when`(repository.getChampions()).thenReturn(Result.Error(error))

        viewModel.refreshChampions()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.isRefreshing)
        assertEquals(error, viewModel.uiState.error)
        assertTrue(viewModel.uiState.champions.isEmpty())
    }
}