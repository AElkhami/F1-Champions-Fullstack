package com.elkhami.f1champions.seasondetails.application

import com.elkhami.f1champions.core.logger.loggerWithPrefix
import com.elkhami.f1champions.seasondetails.domain.SeasonDetailsRepository
import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1champions.seasondetails.domain.service.SeasonDetailsService
import com.elkhami.f1champions.seasondetails.intrastructure.db.entity.SeasonDetailsEntity
import com.elkhami.f1champions.seasondetails.intrastructure.mapper.toDomain
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachePut
import org.springframework.stereotype.Service

@Service
class F1SeasonDetailsService(
    private val seasonDetailsRepository: SeasonDetailsRepository,
    private val cacheManager: CacheManager,
) : SeasonDetailsService {
    val logger = loggerWithPrefix()

    @CachePut(SEASON_DETAILS_CACHE)
    override fun getSeasonDetails(season: String): List<SeasonDetail> {
        return seasonDetailsRepository.findBySeason(season).map { it.toDomain() }
    }

    override fun findDetailsBySeason(season: String): List<SeasonDetailsEntity> {
        return seasonDetailsRepository.findBySeason(season)
    }

    override fun saveSeasonDetails(seasonDetailsEntity: SeasonDetailsEntity) {
        seasonDetailsRepository.deleteBySeasonAndRound(
            seasonDetailsEntity.season,
            seasonDetailsEntity.round,
        )

        seasonDetailsRepository.save(seasonDetailsEntity)
        evictSeasonCache(seasonDetailsEntity.season)
    }

    private fun evictSeasonCache(season: String) {
        cacheManager.getCache(SEASON_DETAILS_CACHE)?.evict(season)
        logger.info("🧹 Evicted cache for season: $season")
    }

    companion object {
        const val SEASON_DETAILS_CACHE = "seasonDetails"
    }
}
