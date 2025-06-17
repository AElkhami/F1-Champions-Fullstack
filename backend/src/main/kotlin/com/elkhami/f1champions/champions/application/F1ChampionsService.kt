package com.elkhami.f1champions.champions.application

import com.elkhami.f1champions.champions.domain.ChampionRepository
import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.champions.domain.service.ChampionsService
import com.elkhami.f1champions.champions.infrastructure.mapper.toDomain
import com.elkhami.f1champions.champions.infrastructure.mapper.toEntity
import com.elkhami.f1champions.core.logger.loggerWithPrefix
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class F1ChampionsService(
    private val championRepo: ChampionRepository,
    private val cacheManager: CacheManager,
) : ChampionsService {
    val logger = loggerWithPrefix()

    @Cacheable(CHAMPIONS_CACHE)
    override fun getChampions(): List<Champion> {
        return championRepo.findAll().map { it.toDomain() }
    }

    override fun findChampionsBySeason(season: String): Champion? {
        return championRepo.findBySeason(season)?.toDomain()
    }

    override fun saveChampion(champion: Champion) {
        val existing = championRepo.findBySeason(champion.season)
        val toSave =
            existing?.copy(
                driverId = champion.driverId,
                driverName = champion.driverName,
                constructor = champion.constructor,
            )?.toDomain() ?: champion

        val id = existing?.id
        championRepo.save(toSave.toEntity(id))
        evictSeasonCache(champion.season)
    }

    private fun evictSeasonCache(season: String) {
        cacheManager.getCache(CHAMPIONS_CACHE)?.clear()
        logger.info("🧹 Evicted cache for season: $season")
    }

    companion object {
        const val CHAMPIONS_CACHE = "champions"
    }
}
