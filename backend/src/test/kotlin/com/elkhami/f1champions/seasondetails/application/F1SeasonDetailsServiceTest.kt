package com.elkhami.f1champions.seasondetails.application

import com.elkhami.f1champions.seasondetails.domain.SeasonDetailsRepository
import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class F1SeasonDetailsServiceTest {
    private val seasonDetailsRepository = mockk<SeasonDetailsRepository>()
    private lateinit var service: F1SeasonDetailsService

    @BeforeEach
    fun setup() {
        service = F1SeasonDetailsService(seasonDetailsRepository)
    }

    @Test
    fun `getSeasonDetails returns mapped domain list`() {
        val season = "2020"
        val entity =
            SeasonDetail(
                season = season,
                round = "1",
                raceName = "Australian GP",
                date = "2020-03-15",
                winnerId = "hamilton",
                winnerName = "Lewis Hamilton",
                constructor = "Mercedes",
            )

        every { seasonDetailsRepository.findBySeason(season) } returns listOf(entity)

        val result = service.findDetailsBySeason(season)

        assertEquals(1, result?.size)
        assertEquals("Australian GP", result?.first()?.raceName)
        assertEquals("Lewis Hamilton", result?.first()?.winnerName)
    }

    @Test
    fun `findDetailsBySeason returns entity list from repository`() {
        val season = "2021"
        val entity =
            SeasonDetail(
                season = season,
                round = "2",
                raceName = "Emilia Romagna GP",
                date = "2021-04-18",
                winnerId = "verstappen",
                winnerName = "Max Verstappen",
                constructor = "Red Bull",
            )

        every { seasonDetailsRepository.findBySeason(season) } returns listOf(entity)

        val result = service.findDetailsBySeason(season)

        assertEquals(1, result?.size)
        assertEquals("Emilia Romagna GP", result?.first()?.raceName)
        assertEquals("Max Verstappen", result?.first()?.winnerName)
    }

    @Test
    fun `saveSeasonDetails deletes by season and round, saves entity, and evicts cache`() {
        val seasonDetail =
            SeasonDetail(
                season = "2022",
                round = "1",
                raceName = "Bahrain GP",
                date = "2022-03-20",
                winnerId = "leclerc",
                winnerName = "Charles Leclerc",
                constructor = "Ferrari",
            )

        every { seasonDetailsRepository.findBySeason("2022") } returns emptyList()
        every { seasonDetailsRepository.deleteBySeasonAndRound("2022", "1") } just Runs
        every { seasonDetailsRepository.save(seasonDetail) } returns seasonDetail

        service.saveSeasonDetails(seasonDetail)

        verifyOrder {
            seasonDetailsRepository.deleteBySeasonAndRound("2022", "1")
            seasonDetailsRepository.save(seasonDetail)
        }
    }
}
