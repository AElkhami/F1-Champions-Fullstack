package com.elkhami.f1champions.champions.application.usecase.seeding

import com.elkhami.f1champions.champions.domain.service.ChampionsClient
import com.elkhami.f1champions.champions.domain.service.ChampionsService
import com.elkhami.f1champions.core.logger.loggerWithPrefix
import org.springframework.stereotype.Component

@Component
class F1SeedChampionUseCase(
    private val championsClient: ChampionsClient,
    private val championsService: ChampionsService,
) : SeedChampionUseCase {
    private val logger = loggerWithPrefix()

    override suspend fun seedIfMissing(year: Int) {
        val season = year.toString()
        if (championExists(season)) {
            logger.info("⏭️ Champion for $season already exists.")
            return
        }
        fetchAndSaveChampion(season, forced = false)
    }

    override suspend fun forceRefresh(year: Int) {
        val season = year.toString()
        fetchAndSaveChampion(season, forced = true)
    }

    private fun championExists(season: String): Boolean {
        return championsService.findChampionsBySeason(season) != null
    }

    private suspend fun fetchAndSaveChampion(
        season: String,
        forced: Boolean,
    ) {
        championsClient.fetchChampion(season.toInt())?.let {
            championsService.saveChampion(it)
            if (forced) {
                logger.info("🔄 Forcefully refreshed champion for $season")
            } else {
                logger.info("✅ Saved champion for $season")
            }
        } ?: logger.warn("⚠️ No champion data found for $season")
    }
}
