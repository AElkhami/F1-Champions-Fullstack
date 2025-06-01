package com.elkhami.f1champions.seasondetails.application

import com.elkhami.f1champions.seasondetails.domain.SeasonDetailsRepository
import com.elkhami.f1champions.seasondetails.intrastructure.db.entity.SeasonDetailsEntity
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class F1SeasonDetailsServiceTest {
    private val seasonDetailsRepository = mockk<SeasonDetailsRepository>()
    private lateinit var service: F1SeasonDetailsService

    @BeforeTest
    fun setup() {
        service = F1SeasonDetailsService(seasonDetailsRepository)
    }

    @Test
    fun `getSeasonDetails returns mapped domain list`() {
        val season = "2020"
        val entity =
            SeasonDetailsEntity(
                season = "2020",
                round = "1",
                raceName = "Australian GP",
                date = "2020-03-15",
                winnerId = "hamilton",
                winnerName = "Lewis Hamilton",
                constructor = "Mercedes",
            )

        every { seasonDetailsRepository.findBySeason(season) } returns listOf(entity)

        val result = service.getSeasonDetails(season)

        assertEquals(1, result.size)
        assertEquals("Australian GP", result.first().raceName)
        assertEquals("Lewis Hamilton", result.first().winnerName)
    }

    @Test
    fun `findDetailsBySeason returns entity list from repository`() {
        val season = "2021"
        val entity =
            SeasonDetailsEntity(
                season = "2021",
                round = "2",
                raceName = "Emilia Romagna GP",
                date = "2021-04-18",
                winnerId = "verstappen",
                winnerName = "Max Verstappen",
                constructor = "Red Bull",
            )

        every { seasonDetailsRepository.findBySeason(season) } returns listOf(entity)

        val result = service.findDetailsBySeason(season)

        assertEquals(1, result.size)
        assertEquals("Emilia Romagna GP", result.first().raceName)
        assertEquals("Max Verstappen", result.first().winnerName)
    }

    @Test
    fun `saveSeasonDetails calls repository save`() {
        val entity =
            SeasonDetailsEntity(
                season = "2022",
                round = "1",
                raceName = "Bahrain GP",
                date = "2022-03-20",
                winnerId = "lechlerc",
                winnerName = "Charles Leclerc",
                constructor = "Ferrari",
            )

        every { seasonDetailsRepository.save(entity) } returns entity

        service.saveSeasonDetails(entity)

        verify { seasonDetailsRepository.save(entity) }
    }
}
