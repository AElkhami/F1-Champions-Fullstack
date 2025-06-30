package com.elkhami.f1champions.core.startup

import com.elkhami.f1champions.champions.application.usecase.seeding.SeedChampionUseCase
import com.elkhami.f1champions.seasondetails.application.usecase.seeding.SeedSeasonDetailsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class AppStartupOrchestratorTest {
    private val seedChampionUseCase = mockk<SeedChampionUseCase>()
    private val seedSeasonDetailsUseCase = mockk<SeedSeasonDetailsUseCase>()

    private lateinit var orchestrator: AppStartupOrchestrator

    @BeforeTest
    fun setup() {
        orchestrator =
            AppStartupOrchestrator(
                seedChampionUseCase = seedChampionUseCase,
                seedSeasonDetailsUseCase = seedSeasonDetailsUseCase,
            )
    }

    @Test
    fun `seed calls seeders for every year in range`() =
        runTest {
            val fromYear = 2020
            val toYear = 2022

            coEvery { seedChampionUseCase.seedIfMissing(any()) } returns Unit
            coEvery { seedSeasonDetailsUseCase.seedIfMissing(any()) } returns Unit

            orchestrator.startUpSeed(fromYear = fromYear, toYear = toYear)

            coVerify(exactly = 1) { seedChampionUseCase.seedIfMissing(2020) }
            coVerify(exactly = 1) { seedSeasonDetailsUseCase.seedIfMissing(2020) }

            coVerify(exactly = 1) { seedChampionUseCase.seedIfMissing(2021) }
            coVerify(exactly = 1) { seedSeasonDetailsUseCase.seedIfMissing(2021) }

            coVerify(exactly = 1) { seedChampionUseCase.seedIfMissing(2022) }
            coVerify(exactly = 1) { seedSeasonDetailsUseCase.seedIfMissing(2022) }
        }

    @Test
    fun `seed logs error when champion seeding fails but still seeds season details`() =
        runTest {
            val fromYear = 2020
            val toYear = 2021

            coEvery { seedChampionUseCase.seedIfMissing(2020) } throws RuntimeException("Boom!")
            coEvery { seedChampionUseCase.seedIfMissing(2021) } returns Unit

            coEvery { seedSeasonDetailsUseCase.seedIfMissing(any()) } returns Unit

            orchestrator.startUpSeed(fromYear, toYear)

            coVerify { seedChampionUseCase.seedIfMissing(2020) }
            coVerify { seedChampionUseCase.seedIfMissing(2021) }

            coVerify { seedSeasonDetailsUseCase.seedIfMissing(2020) } // ✅ was called!
            coVerify { seedSeasonDetailsUseCase.seedIfMissing(2021) }
        }

    @Test
    fun `refreshChampion delegates to championSeeder forceRefresh`() =
        runTest {
            val year = 2023
            coEvery { seedChampionUseCase.forceRefresh(year) } returns Unit

            orchestrator.refreshChampion(year)

            coVerify(exactly = 1) { seedChampionUseCase.forceRefresh(year) }
        }

    @Test
    fun `refreshSeasons delegates to seasonDetailsSeeder forceRefresh`() =
        runTest {
            val year = 2023
            coEvery { seedSeasonDetailsUseCase.forceRefresh(year) } returns Unit

            orchestrator.refreshSeasons(year)

            coVerify(exactly = 1) { seedSeasonDetailsUseCase.forceRefresh(year) }
        }

    @Test
    fun `refreshChampion logs error when forceRefresh throws`() =
        runTest {
            val year = 2024
            coEvery { seedChampionUseCase.forceRefresh(year) } throws RuntimeException("Boom!")

            orchestrator.refreshChampion(year)

            coVerify { seedChampionUseCase.forceRefresh(year) }
        }

    @Test
    fun `refreshSeasons logs error when forceRefresh throws`() =
        runTest {
            val year = 2024
            coEvery { seedSeasonDetailsUseCase.forceRefresh(year) } throws RuntimeException("Boom!")

            orchestrator.refreshSeasons(year)

            coVerify { seedSeasonDetailsUseCase.forceRefresh(year) }
        }
}
