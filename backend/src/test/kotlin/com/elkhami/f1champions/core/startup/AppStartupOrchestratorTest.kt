package com.elkhami.f1champions.core.startup

import com.elkhami.f1champions.champions.application.seeding.ChampionSeeder
import com.elkhami.f1champions.seasondetails.application.seeding.SeasonDetailsSeeder
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class AppStartupOrchestratorTest {
    private val championSeeder = mockk<ChampionSeeder>()
    private val seasonDetailsSeeder = mockk<SeasonDetailsSeeder>()

    private lateinit var orchestrator: AppStartupOrchestrator

    @BeforeTest
    fun setup() {
        orchestrator =
            AppStartupOrchestrator(
                championSeeder = championSeeder,
                seasonDetailsSeeder = seasonDetailsSeeder,
            )
    }

    @Test
    fun `seed calls seeders for every year in range`() =
        runTest {
            val fromYear = 2020
            val toYear = 2022

            coEvery { championSeeder.seedIfMissing(any()) } returns Unit
            coEvery { seasonDetailsSeeder.seedIfMissing(any()) } returns Unit

            orchestrator.startUpSeed(fromYear = fromYear, toYear = toYear)

            coVerify(exactly = 1) { championSeeder.seedIfMissing(2020) }
            coVerify(exactly = 1) { seasonDetailsSeeder.seedIfMissing(2020) }

            coVerify(exactly = 1) { championSeeder.seedIfMissing(2021) }
            coVerify(exactly = 1) { seasonDetailsSeeder.seedIfMissing(2021) }

            coVerify(exactly = 1) { championSeeder.seedIfMissing(2022) }
            coVerify(exactly = 1) { seasonDetailsSeeder.seedIfMissing(2022) }
        }

    @Test
    fun `seed logs error when champion seeding fails but still seeds season details`() =
        runTest {
            val fromYear = 2020
            val toYear = 2021

            coEvery { championSeeder.seedIfMissing(2020) } throws RuntimeException("Boom!")
            coEvery { championSeeder.seedIfMissing(2021) } returns Unit

            coEvery { seasonDetailsSeeder.seedIfMissing(any()) } returns Unit

            orchestrator.startUpSeed(fromYear, toYear)

            coVerify { championSeeder.seedIfMissing(2020) }
            coVerify { championSeeder.seedIfMissing(2021) }

            coVerify { seasonDetailsSeeder.seedIfMissing(2020) } // ✅ was called!
            coVerify { seasonDetailsSeeder.seedIfMissing(2021) }
        }

    @Test
    fun `seedSeasons logs error when seeding season details fails`() =
        runTest {
            val year = 2025
            coEvery { seasonDetailsSeeder.seedIfMissing(year) } throws RuntimeException("Failed to seed season details")

            orchestrator.seedSeasons(year)

            coVerify { seasonDetailsSeeder.seedIfMissing(year) }
        }

    @Test
    fun `refreshChampion delegates to championSeeder forceRefresh`() =
        runTest {
            val year = 2023
            coEvery { championSeeder.forceRefresh(year) } returns Unit

            orchestrator.refreshChampion(year)

            coVerify(exactly = 1) { championSeeder.forceRefresh(year) }
        }

    @Test
    fun `refreshSeasons delegates to seasonDetailsSeeder forceRefresh`() =
        runTest {
            val year = 2023
            coEvery { seasonDetailsSeeder.forceRefresh(year) } returns Unit

            orchestrator.refreshSeasons(year)

            coVerify(exactly = 1) { seasonDetailsSeeder.forceRefresh(year) }
        }

    @Test
    fun `refreshChampion logs error when forceRefresh throws`() =
        runTest {
            val year = 2024
            coEvery { championSeeder.forceRefresh(year) } throws RuntimeException("Boom!")

            orchestrator.refreshChampion(year)

            coVerify { championSeeder.forceRefresh(year) }
        }

    @Test
    fun `refreshSeasons logs error when forceRefresh throws`() =
        runTest {
            val year = 2024
            coEvery { seasonDetailsSeeder.forceRefresh(year) } throws RuntimeException("Boom!")

            orchestrator.refreshSeasons(year)

            coVerify { seasonDetailsSeeder.forceRefresh(year) }
        }
}
