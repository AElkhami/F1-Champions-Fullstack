package com.elkhami.f1champions.seasondetails.application.usecase.seeding

import com.elkhami.f1champions.core.logger.loggerWithPrefix
import com.elkhami.f1champions.core.network.ApiResponse
import com.elkhami.f1champions.seasondetails.domain.service.SeasonDetailsClient
import com.elkhami.f1champions.seasondetails.domain.service.SeasonDetailsService
import org.springframework.stereotype.Component

@Component
class F1SeedSeasonDetailsUseCase(
    private val seasonDetailsClient: SeasonDetailsClient,
    private val seasonDetailsService: SeasonDetailsService,
) : SeedSeasonDetailsUseCase {
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
        return seasonDetailsService.findDetailsBySeason(season)?.isNotEmpty() ?: false
    }

    private suspend fun fetchAndSaveSeasonDetails(season: String) {
        when (val response = seasonDetailsClient.fetchSeasonDetails(season)) {
            is ApiResponse.Success -> {
                val winners = response.data
                if (winners.isNullOrEmpty()) {
                    logger.warn("⚠️ No season details found for $season")
                } else {
                    try {
                        winners.forEach { seasonDetail ->
                            try {
                                seasonDetailsService.saveSeasonDetails(seasonDetail)
                            } catch (e: Exception) {
                                logger.error("❌ Failed to save season detail for $season round ${seasonDetail.round}: ${e.message}")
                            }
                        }
                        seasonDetailsService.evictSeasonCache(season)
                        logger.info("✅ Saved ${winners.size} race winners for season $season")
                    } catch (e: Exception) {
                        logger.error("❌ Failed to process season details for $season: ${e.message}")
                    }
                }
            }
            is ApiResponse.Error -> {
                logger.error("❌ Failed to fetch season details for $season: ${response.message}")
            }
        }
    }
}
