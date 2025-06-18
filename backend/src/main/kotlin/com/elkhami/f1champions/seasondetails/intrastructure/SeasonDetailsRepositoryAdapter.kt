package com.elkhami.f1champions.seasondetails.intrastructure

import com.elkhami.f1champions.core.logger.loggerWithPrefix
import com.elkhami.f1champions.seasondetails.domain.SeasonDetailsRepository
import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1champions.seasondetails.intrastructure.mapper.toDomain
import com.elkhami.f1champions.seasondetails.intrastructure.mapper.toEntity
import com.elkhami.f1champions.seasondetails.intrastructure.repository.SeasonDetailsJpaRepository
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository

@Repository
class SeasonDetailsRepositoryAdapter(
    private val jpaRepository: SeasonDetailsJpaRepository,
    private val cacheManager: CacheManager,
) : SeasonDetailsRepository {
    val logger = loggerWithPrefix()

    @Cacheable(SEASON_DETAILS_CACHE)
    override fun findBySeason(season: String): List<SeasonDetail>? {
        logger.info("🐌 fetching season details from DB")
        return jpaRepository.findBySeason(season)?.map { it.toDomain() }
    }

    override fun deleteBySeasonAndRound(
        season: String,
        round: String,
    ) {
        return jpaRepository.deleteBySeasonAndRound(season, round)
    }

    override fun save(seasonDetail: SeasonDetail): SeasonDetail {
        return jpaRepository.save(seasonDetail.toEntity()).toDomain()
    }

    override fun evictSeasonCache(season: String) {
        cacheManager.getCache(SEASON_DETAILS_CACHE)?.evict(season)
        logger.info("🧹 Evicted Season Details cache for season: $season")
    }

    companion object {
        const val SEASON_DETAILS_CACHE = "seasonDetails"
    }
}
