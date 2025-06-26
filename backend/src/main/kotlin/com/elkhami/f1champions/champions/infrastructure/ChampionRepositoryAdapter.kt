package com.elkhami.f1champions.champions.infrastructure

import com.elkhami.f1champions.champions.domain.ChampionRepository
import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.champions.infrastructure.mapper.toDomain
import com.elkhami.f1champions.champions.infrastructure.mapper.toEntity
import com.elkhami.f1champions.champions.infrastructure.repository.ChampionJpaRepository
import com.elkhami.f1champions.core.logger.loggerWithPrefix
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository

@Repository
class ChampionRepositoryAdapter(
    private val jpaRepository: ChampionJpaRepository,
    private val cacheManager: CacheManager,
) : ChampionRepository {
    val logger = loggerWithPrefix()

    @Cacheable(CHAMPIONS_CACHE)
    override fun findAll(): List<Champion> {
        logger.info("🐌 fetching champions from DB")
        return jpaRepository.findAll().map { it.toDomain() }
    }

    override fun findBySeason(season: String): Champion? {
        return jpaRepository.findBySeason(season)?.toDomain()
    }

    override fun save(champion: Champion): Champion {
        return jpaRepository.save(champion.toEntity()).toDomain()
    }

    override fun evictSeasonCache(season: String) {
        val cache = cacheManager.getCache(CHAMPIONS_CACHE)
        cache?.evict(season)
        logger.info("🧹 Evicted Champion cache for season: $season")
    }

    companion object {
        const val CHAMPIONS_CACHE = "champions"
    }
}
