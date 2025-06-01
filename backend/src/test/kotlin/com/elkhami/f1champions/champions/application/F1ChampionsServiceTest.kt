package com.elkhami.f1champions.champions.application

import com.elkhami.f1champions.champions.domain.ChampionRepository
import com.elkhami.f1champions.champions.infrastructure.db.entity.ChampionEntity
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class F1ChampionsServiceTest {
    private val championRepository = mockk<ChampionRepository>()
    private lateinit var service: F1ChampionsService

    @BeforeTest
    fun setup() {
        service = F1ChampionsService(championRepository)
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
    fun `saveChampion calls repository save`() {
        val entity =
            ChampionEntity(
                season = "2022",
                driverId = "lecerc",
                driverName = "Charles Leclerc",
                constructor = "Ferrari",
            )

        every { championRepository.save(entity) } returns entity

        service.saveChampion(entity)

        verify { championRepository.save(entity) }
    }
}
