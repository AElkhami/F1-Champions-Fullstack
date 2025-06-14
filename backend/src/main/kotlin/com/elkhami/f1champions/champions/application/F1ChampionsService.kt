package com.elkhami.f1champions.champions.application

import com.elkhami.f1champions.champions.domain.ChampionRepository
import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.champions.domain.service.ChampionsService
import com.elkhami.f1champions.champions.infrastructure.db.entity.ChampionEntity
import com.elkhami.f1champions.champions.infrastructure.mapper.toDomain
import com.elkhami.f1champions.core.logger.loggerWithPrefix
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachePut
import org.springframework.stereotype.Service

@Service
class F1ChampionsService(
    private val championRepo: ChampionRepository,
    private val cacheManager: CacheManager,
) : ChampionsService {
    val logger = loggerWithPrefix()

    @CachePut(CHAMPIONS_CACHE)
    override fun getChampions(): List<Champion> {
        return championRepo.findAll().map { it.toDomain() }
    }

    override fun findChampionsBySeason(season: String): ChampionEntity? {
        return championRepo.findBySeason(season)
    }

    override fun saveChampion(championEntity: ChampionEntity) {
        val existing = championRepo.findBySeason(championEntity.season)
        val toSave =
            existing?.copy(
                driverId = championEntity.driverId,
                driverName = championEntity.driverName,
                constructor = championEntity.constructor,
            ) ?: championEntity

        championRepo.save(toSave)
        evictSeasonCache(championEntity.season)
    }

    private fun evictSeasonCache(season: String) {
        cacheManager.getCache(CHAMPIONS_CACHE)?.evict(season)
        logger.info("🧹 Evicted cache for season: $season")
    }

    companion object {
        const val CHAMPIONS_CACHE = "champions"
    }
}
