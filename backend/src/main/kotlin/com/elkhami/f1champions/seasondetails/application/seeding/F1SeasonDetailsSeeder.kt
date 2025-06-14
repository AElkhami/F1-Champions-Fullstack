package com.elkhami.f1champions.seasondetails.application.seeding

import com.elkhami.f1champions.core.logger.loggerWithPrefix
import com.elkhami.f1champions.seasondetails.domain.service.SeasonDetailsService
import com.elkhami.f1champions.seasondetails.intrastructure.api.SeasonDetailsClient
import com.elkhami.f1champions.seasondetails.intrastructure.mapper.toEntity
import org.springframework.stereotype.Component

@Component
class F1SeasonDetailsSeeder(
    private val seasonDetailsClient: SeasonDetailsClient,
    private val seasonDetailsService: SeasonDetailsService,
) : SeasonDetailsSeeder {
    private val logger = loggerWithPrefix()

    override suspend fun seedIfMissing(year: Int) {
        val season = year.toString()
        if (seasonDetailsExist(season)) {
            logger.info("⏭️ Season details for $season already exist.")
            return
        }
        fetchAndSaveSeasonDetails(season)
    }

    override suspend fun forceRefresh(year: Int) {
        val season = year.toString()
        logger.info("🔄 Forcefully refreshing season details for $season")
        fetchAndSaveSeasonDetails(season)
    }

    private suspend fun seasonDetailsExist(season: String): Boolean {
        return seasonDetailsService.findDetailsBySeason(season).isNotEmpty()
    }

    private suspend fun fetchAndSaveSeasonDetails(season: String) {
        val winners = seasonDetailsClient.fetchSeasonDetails(season)
        if (winners.isEmpty()) {
            logger.warn("⚠️ No season details found for $season")
        } else {
            winners.forEach { seasonDetailsService.saveSeasonDetails(it.toEntity()) }
            logger.info("✅ Saved ${winners.size} race winners for season $season")
        }
    }
}
