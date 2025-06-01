package com.elkhami.f1champions.seasondetails.application.seeding

import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1champions.seasondetails.domain.service.SeasonDetailsService
import com.elkhami.f1champions.seasondetails.intrastructure.api.SeasonDetailsClient
import com.elkhami.f1champions.seasondetails.intrastructure.db.entity.SeasonDetailsEntity
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

class F1SeasonDetailsSeederTest {
    private val seasonDetailsClient = mockk<SeasonDetailsClient>()
    private val seasonDetailsService = mockk<SeasonDetailsService>()

    private lateinit var seeder: F1SeasonDetailsSeeder

    @BeforeTest
    fun setup() {
        seeder =
            F1SeasonDetailsSeeder(
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
                    SeasonDetailsEntity(
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
            coEvery { seasonDetailsClient.fetchSeasonDetails(season) } returns listOf(detail)
            every { seasonDetailsService.saveSeasonDetails(any()) } just Runs

            seeder.seedIfMissing(year)

            verify { seasonDetailsService.findDetailsBySeason(season) }
            coVerify { seasonDetailsClient.fetchSeasonDetails(season) }
            verify { seasonDetailsService.saveSeasonDetails(match { it.raceName == "Bahrain GP" }) }
        }

    @Test
    fun `seedIfMissing does nothing when client returns empty list`() =
        runTest {
            val season = "2022"

            every { seasonDetailsService.findDetailsBySeason(season) } returns emptyList()
            coEvery { seasonDetailsClient.fetchSeasonDetails(season) } returns emptyList()

            seeder.seedIfMissing(2022)

            verify { seasonDetailsService.findDetailsBySeason(season) }
            coVerify { seasonDetailsClient.fetchSeasonDetails(season) }
            verify(exactly = 0) { seasonDetailsService.saveSeasonDetails(any()) }
        }
}
