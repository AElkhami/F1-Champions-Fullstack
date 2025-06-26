package com.elkhami.f1champions.champions.infrastructure

import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.champions.infrastructure.db.entity.ChampionEntity
import com.elkhami.f1champions.champions.infrastructure.mapper.toEntity
import com.elkhami.f1champions.champions.infrastructure.repository.ChampionJpaRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ChampionRepositoryAdapterTest {
    private val jpaRepository = mockk<ChampionJpaRepository>()
    private val cacheManager = mockk<CacheManager>()
    private val cache = mockk<Cache>(relaxed = true)
    private lateinit var adapter: ChampionRepositoryAdapter

    @BeforeTest
    fun setup() {
        every { cacheManager.getCache(ChampionRepositoryAdapter.CHAMPIONS_CACHE) } returns cache
        adapter = ChampionRepositoryAdapter(jpaRepository, cacheManager)
    }

    @Test
    fun `findAll returns mapped champions`() {
        val entity = ChampionEntity(season = "2021", driverId = "max", driverName = "Max Verstappen", constructor = "Red Bull")
        every { jpaRepository.findAll() } returns listOf(entity)

        val result = adapter.findAll()

        assertEquals(1, result.size)
        assertEquals("max", result[0].driverId)
        assertEquals("Max Verstappen", result[0].driverName)
        assertEquals("Red Bull", result[0].constructor)
    }

    @Test
    fun `findBySeason returns mapped champion`() {
        val entity = ChampionEntity(season = "2021", driverId = "max", driverName = "Max Verstappen", constructor = "Red Bull")
        every { jpaRepository.findBySeason("2021") } returns entity

        val result = adapter.findBySeason("2021")

        assertEquals("max", result?.driverId)
        assertEquals("Max Verstappen", result?.driverName)
        assertEquals("Red Bull", result?.constructor)
    }

    @Test
    fun `findBySeason returns null if not found`() {
        every { jpaRepository.findBySeason("1999") } returns null

        val result = adapter.findBySeason("1999")

        assertNull(result)
    }

    @Test
    fun `save persists and returns mapped champion`() {
        val champion = Champion("2022", "leclerc", "Charles Leclerc", "Ferrari")
        val entity = champion.toEntity()
        every { jpaRepository.save(any()) } returns entity

        val result = adapter.save(champion)

        assertEquals("leclerc", result.driverId)
        assertEquals("Charles Leclerc", result.driverName)
        assertEquals("Ferrari", result.constructor)
        verify { jpaRepository.save(match { it.season == "2022" && it.driverId == "leclerc" }) }
    }

    @Test
    fun `evictSeasonCache evicts specific season from cache`() {
        every { cache.evict("2021") } just Runs

        adapter.evictSeasonCache("2021")

        verify {
            cacheManager.getCache(ChampionRepositoryAdapter.CHAMPIONS_CACHE)
            cache.evict("2021")
        }
    }

    @Test
    fun `evictSeasonCache handles null cache gracefully`() {
        every { cacheManager.getCache(ChampionRepositoryAdapter.CHAMPIONS_CACHE) } returns null

        adapter.evictSeasonCache("2021")

        verify { cacheManager.getCache(ChampionRepositoryAdapter.CHAMPIONS_CACHE) }
    }
} 
