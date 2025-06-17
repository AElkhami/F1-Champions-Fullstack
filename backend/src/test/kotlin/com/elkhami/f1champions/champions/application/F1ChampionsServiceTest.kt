package com.elkhami.f1champions.champions.application

import com.elkhami.f1champions.champions.domain.ChampionRepository
import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.champions.infrastructure.db.entity.ChampionEntity
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verifyOrder
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class F1ChampionsServiceTest {
    private val championRepository = mockk<ChampionRepository>()
    private val cacheManager = mockk<CacheManager>()
    private val cache = mockk<Cache>(relaxed = true)
    private lateinit var service: F1ChampionsService

    @BeforeTest
    fun setup() {
        every { cacheManager.getCache(F1ChampionsService.CHAMPIONS_CACHE) } returns cache
        service = F1ChampionsService(championRepository, cacheManager)
    }

    @Test
    fun `getChampions returns mapped domain list`() {
        val entity =
            ChampionEntity(
                season = "2020",
                driverId = "hamilton",
                driverName = "Lewis Hamilton",
                constructor = "Mercedes",
            )

        every { championRepository.findAll() } returns listOf(entity)

        val result = service.getChampions()

        assertEquals(1, result.size)
        assertEquals("Lewis Hamilton", result.first().driverName)
    }

    @Test
    fun `findChampionsBySeason returns correct entity`() {
        val season = "2021"
        val entity =
            ChampionEntity(
                season = season,
                driverId = "verstappen",
                driverName = "Max Verstappen",
                constructor = "Red Bull",
            )

        every { championRepository.findBySeason(season) } returns entity

        val result = service.findChampionsBySeason(season)

        assertNotNull(result)
        assertEquals("Max Verstappen", result.driverName)
    }

    @Test
    fun `findChampionsBySeason returns null when no match found`() {
        val season = "1999"

        every { championRepository.findBySeason(season) } returns null

        val result = service.findChampionsBySeason(season)

        assertNull(result)
    }

    @Test
    fun `saveChampion checks for existing, saves updated entity, and evicts cache`() {
        val existing =
            ChampionEntity(
                season = "2022",
                driverId = "hamilton",
                driverName = "Lewis Hamilton",
                constructor = "Mercedes",
            )

        val updated =
            Champion(
                season = "2022",
                driverId = "leclerc",
                driverName = "Charles Leclerc",
                constructor = "Ferrari",
            )

        val expectedToSave =
            existing.copy(
                driverId = updated.driverId,
                driverName = updated.driverName,
                constructor = updated.constructor,
            )

        every { championRepository.findBySeason("2022") } returns existing
        every { championRepository.save(expectedToSave) } returns expectedToSave
        every { cacheManager.getCache(F1ChampionsService.CHAMPIONS_CACHE) } returns cache
        every { cache.clear() } just Runs

        service.saveChampion(updated)

        verifyOrder {
            championRepository.findBySeason("2022")
            championRepository.save(expectedToSave)
            cacheManager.getCache(F1ChampionsService.CHAMPIONS_CACHE)
            cache.clear()
        }
    }
}
