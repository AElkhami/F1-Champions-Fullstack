package com.elkhami.f1champions.champions.application.seeding

import com.elkhami.f1champions.champions.application.usecase.seeding.F1SeedChampionUseCase
import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.champions.domain.service.ChampionsClient
import com.elkhami.f1champions.champions.domain.service.ChampionsService
import com.elkhami.f1champions.core.network.ApiResponse
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class F1SeedChampionUseCaseTest {
    private val championsClient = mockk<ChampionsClient>()
    private val championsService = mockk<ChampionsService>()

    private lateinit var seeder: F1SeedChampionUseCase

    @BeforeTest
    fun setup() {
        seeder =
            F1SeedChampionUseCase(
                championsClient = championsClient,
                championsService = championsService,
            )
    }

    @Test
    fun `seedIfMissing skips when champion already exists`() =
        runTest {
            val season = "2020"

            every { championsService.findChampionsBySeason(season) } returns
                Champion(
                    season = season,
                    driverId = "hamilton",
                    driverName = "Lewis Hamilton",
                    constructor = "Mercedes",
                )

            seeder.seedIfMissing(2020)

            verify(exactly = 1) { championsService.findChampionsBySeason(season) }
            coVerify(exactly = 0) { championsClient.fetchChampion(any()) }
            verify(exactly = 0) { championsService.saveChampion(any()) }
        }

    @Test
    fun `seedIfMissing saves when champion does not exist`() =
        runTest {
            val year = 2021
            val season = year.toString()
            val champion =
                Champion(
                    season = season,
                    driverId = "verstappen",
                    driverName = "Max Verstappen",
                    constructor = "Red Bull",
                )

            every { championsService.findChampionsBySeason(season) } returns null
            coEvery { championsClient.fetchChampion(year) } returns ApiResponse.success(champion)
            every { championsService.saveChampion(any()) } just Runs

            seeder.seedIfMissing(year)

            verify { championsService.findChampionsBySeason(season) }
            coVerify { championsClient.fetchChampion(year) }
            verify { championsService.saveChampion(match { it.driverName == "Max Verstappen" }) }
        }

    @Test
    fun `seedIfMissing does nothing if client returns error`() =
        runTest {
            val year = 2019
            val season = year.toString()

            every { championsService.findChampionsBySeason(season) } returns null
            coEvery { championsClient.fetchChampion(year) } returns ApiResponse.error("API Error")

            seeder.seedIfMissing(year)

            verify { championsService.findChampionsBySeason(season) }
            coVerify { championsClient.fetchChampion(year) }
            verify(exactly = 0) { championsService.saveChampion(any()) }
        }

    @Test
    fun `forceRefresh fetches and saves champion regardless of existence`() =
        runTest {
            val year = 2022
            val season = year.toString()

            val champion =
                Champion(
                    season = season,
                    driverId = "leclerc",
                    driverName = "Charles Leclerc",
                    constructor = "Ferrari",
                )

            coEvery { championsClient.fetchChampion(year) } returns ApiResponse.success(champion)
            every { championsService.saveChampion(any()) } just Runs

            seeder.forceRefresh(year)

            coVerify { championsClient.fetchChampion(year) }
            verify { championsService.saveChampion(match { it.driverName == "Charles Leclerc" }) }
        }
}
