package com.elkhami.f1champions.seasondetails.application

import com.elkhami.f1champions.seasondetails.domain.SeasonDetailsRepository
import com.elkhami.f1champions.seasondetails.intrastructure.db.entity.SeasonDetailsEntity
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager

class F1SeasonDetailsServiceTest {
    private val seasonDetailsRepository = mockk<SeasonDetailsRepository>()
    private val cacheManager = mockk<CacheManager>()
    private val cache = mockk<Cache>(relaxed = true)
    private lateinit var service: F1SeasonDetailsService

    @BeforeEach
    fun setup() {
        every { cacheManager.getCache(F1SeasonDetailsService.SEASON_DETAILS_CACHE) } returns cache
        service = F1SeasonDetailsService(seasonDetailsRepository, cacheManager)
    }

    @Test
    fun `getSeasonDetails returns mapped domain list`() {
        val season = "2020"
        val entity =
            SeasonDetailsEntity(
                season = season,
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

        assertEquals(1, result.size)
        assertEquals("Emilia Romagna GP", result.first().raceName)
        assertEquals("Max Verstappen", result.first().winnerName)
    }

    @Test
    fun `saveSeasonDetails calls repository save and evicts cache`() {
        val entity =
            SeasonDetailsEntity(
                season = "2022",
                round = "1",
                raceName = "Bahrain GP",
                date = "2022-03-20",
                winnerId = "leclerc",
                winnerName = "Charles Leclerc",
                constructor = "Ferrari",
            )

        every { seasonDetailsRepository.save(entity) } returns entity
        every { cache.evict(entity.season) } just Runs

        service.saveSeasonDetails(entity)

        verify { seasonDetailsRepository.save(entity) }
        verify { cacheManager.getCache(F1SeasonDetailsService.SEASON_DETAILS_CACHE) }
        verify { cache.evict("2022") }
    }
}
