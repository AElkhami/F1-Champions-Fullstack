package com.elkhami.f1champions.seasondetails.application.seeding

import com.elkhami.f1champions.core.network.ApiResponse
import com.elkhami.f1champions.seasondetails.application.usecase.seeding.F1SeedSeasonDetailsUseCase
import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1champions.seasondetails.domain.service.SeasonDetailsClient
import com.elkhami.f1champions.seasondetails.domain.service.SeasonDetailsService
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

class F1SeedSeasonDetailsUseCaseTest {
    private val seasonDetailsClient = mockk<SeasonDetailsClient>()
    private val seasonDetailsService = mockk<SeasonDetailsService>()

    private lateinit var seeder: F1SeedSeasonDetailsUseCase

    @BeforeTest
    fun setup() {
        seeder =
            F1SeedSeasonDetailsUseCase(
                seasonDetailsClient = seasonDetailsClient,
                seasonDetailsService = seasonDetailsService,
            )
    }

    @Test
    fun `seedIfMissing does nothing if season already exists`() =
        runTest {
            val season = "2020"

            every { seasonDetailsService.findDetailsBySeason(season) } returns
                listOf(
                    SeasonDetail(
                        season = season,
                        round = "1",
                        raceName = "Australian GP",
                        date = "2020-03-15",
                        winnerId = "hamilton",
                        winnerName = "Lewis Hamilton",
                        constructor = "Mercedes",
                    ),
                )

            seeder.seedIfMissing(2020)

            verify(exactly = 1) { seasonDetailsService.findDetailsBySeason(season) }
            coVerify(exactly = 0) { seasonDetailsClient.fetchSeasonDetails(any()) }
            verify(exactly = 0) { seasonDetailsService.saveSeasonDetails(any()) }
        }

    @Test
    fun `seedIfMissing fetches and saves season details when missing`() =
        runTest {
            val year = 2021
            val season = year.toString()

            val detail =
                SeasonDetail(
                    season = season,
                    round = "1",
                    raceName = "Bahrain GP",
                    date = "2021-03-28",
                    winnerId = "hamilton",
                    winnerName = "Lewis Hamilton",
                    constructor = "Mercedes",
                )

            every { seasonDetailsService.findDetailsBySeason(season) } returns emptyList()
            coEvery { seasonDetailsClient.fetchSeasonDetails(season) } returns ApiResponse.success(listOf(detail))
            every { seasonDetailsService.saveSeasonDetails(any()) } just Runs
            every { seasonDetailsService.evictSeasonCache(season) } just Runs

            seeder.seedIfMissing(year)

            verify { seasonDetailsService.findDetailsBySeason(season) }
            coVerify { seasonDetailsClient.fetchSeasonDetails(season) }
            verify { seasonDetailsService.saveSeasonDetails(match { it.raceName == "Bahrain GP" }) }
            verify { seasonDetailsService.evictSeasonCache(season) }
        }

    @Test
    fun `seedIfMissing does nothing when client returns error`() =
        runTest {
            val season = "2022"

            every { seasonDetailsService.findDetailsBySeason(season) } returns emptyList()
            coEvery { seasonDetailsClient.fetchSeasonDetails(season) } returns ApiResponse.error("API Error")

            seeder.seedIfMissing(2022)

            verify { seasonDetailsService.findDetailsBySeason(season) }
            coVerify { seasonDetailsClient.fetchSeasonDetails(season) }
            verify(exactly = 0) { seasonDetailsService.saveSeasonDetails(any()) }
        }

    @Test
    fun `forceRefresh always fetches and saves season details`() =
        runTest {
            val year = 2023
            val season = year.toString()

            val detail =
                SeasonDetail(
                    season = season,
                    round = "3",
                    raceName = "Australian GP",
                    date = "2023-04-02",
                    winnerId = "verstappen",
                    winnerName = "Max Verstappen",
                    constructor = "Red Bull",
                )

            coEvery { seasonDetailsClient.fetchSeasonDetails(season) } returns ApiResponse.success(listOf(detail))
            every { seasonDetailsService.saveSeasonDetails(any()) } just Runs
            every { seasonDetailsService.evictSeasonCache(season) } just Runs

            seeder.forceRefresh(year)

            coVerify { seasonDetailsClient.fetchSeasonDetails(season) }
            verify { seasonDetailsService.saveSeasonDetails(match { it.winnerName == "Max Verstappen" }) }
            verify { seasonDetailsService.evictSeasonCache(season) }
        }
}
