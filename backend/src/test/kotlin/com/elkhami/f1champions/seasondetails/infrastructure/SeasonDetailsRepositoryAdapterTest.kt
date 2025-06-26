package com.elkhami.f1champions.seasondetails.infrastructure

import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1champions.seasondetails.intrastructure.SeasonDetailsRepositoryAdapter
import com.elkhami.f1champions.seasondetails.intrastructure.db.entity.SeasonDetailsEntity
import com.elkhami.f1champions.seasondetails.intrastructure.mapper.toEntity
import com.elkhami.f1champions.seasondetails.intrastructure.repository.SeasonDetailsJpaRepository
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

class SeasonDetailsRepositoryAdapterTest {
    private val jpaRepository = mockk<SeasonDetailsJpaRepository>()
    private val cacheManager = mockk<CacheManager>()
    private val cache = mockk<Cache>(relaxed = true)
    private lateinit var adapter: SeasonDetailsRepositoryAdapter

    @BeforeTest
    fun setup() {
        every { cacheManager.getCache(SeasonDetailsRepositoryAdapter.SEASON_DETAILS_CACHE) } returns cache
        adapter = SeasonDetailsRepositoryAdapter(jpaRepository, cacheManager)
    }

    @Test
    fun `findBySeason returns mapped season details`() {
        val entity =
            SeasonDetailsEntity(
                season = "2021",
                round = "1",
                raceName = "Bahrain GP",
                date = "2021-03-28",
                winnerId = "max",
                winnerName = "Max Verstappen",
                constructor = "Red Bull",
            )
        every { jpaRepository.findBySeason("2021") } returns listOf(entity)

        val result = adapter.findBySeason("2021")

        assertEquals(1, result?.size)
        assertEquals("Bahrain GP", result?.first()?.raceName)
        assertEquals("Max Verstappen", result?.first()?.winnerName)
        assertEquals("Red Bull", result?.first()?.constructor)
    }

    @Test
    fun `findBySeason returns null if not found`() {
        every { jpaRepository.findBySeason("1999") } returns null

        val result = adapter.findBySeason("1999")

        assertNull(result)
    }

    @Test
    fun `findBySeason returns empty list when no details exist`() {
        every { jpaRepository.findBySeason("2020") } returns emptyList()

        val result = adapter.findBySeason("2020")

        assertEquals(0, result?.size)
    }

    @Test
    fun `deleteBySeasonAndRound delegates to JPA repository`() {
        every { jpaRepository.deleteBySeasonAndRound("2021", "1") } just Runs

        adapter.deleteBySeasonAndRound("2021", "1")

        verify { jpaRepository.deleteBySeasonAndRound("2021", "1") }
    }

    @Test
    fun `save persists and returns mapped season detail`() {
        val detail = SeasonDetail("2022", "2", "Jeddah GP", "2022-03-27", "leclerc", "Charles Leclerc", "Ferrari")
        val entity = detail.toEntity()
        every { jpaRepository.save(any()) } returns entity

        val result = adapter.save(detail)

        assertEquals("Jeddah GP", result.raceName)
        assertEquals("Charles Leclerc", result.winnerName)
        assertEquals("Ferrari", result.constructor)
        verify { jpaRepository.save(match { it.season == "2022" && it.round == "2" }) }
    }

    @Test
    fun `evictSeasonCache evicts specific season from cache`() {
        every { cache.evict("2021") } just Runs

        adapter.evictSeasonCache("2021")

        verify {
            cacheManager.getCache(SeasonDetailsRepositoryAdapter.SEASON_DETAILS_CACHE)
            cache.evict("2021")
        }
    }

    @Test
    fun `evictSeasonCache handles null cache gracefully`() {
        every { cacheManager.getCache(SeasonDetailsRepositoryAdapter.SEASON_DETAILS_CACHE) } returns null

        adapter.evictSeasonCache("2021")

        verify { cacheManager.getCache(SeasonDetailsRepositoryAdapter.SEASON_DETAILS_CACHE) }
    }
} 
