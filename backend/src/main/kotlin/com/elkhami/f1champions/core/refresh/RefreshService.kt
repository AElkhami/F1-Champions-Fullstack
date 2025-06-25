package com.elkhami.f1champions.core.refresh

import com.elkhami.f1champions.core.logger.loggerWithPrefix
import com.elkhami.f1champions.core.startup.AppStartupOrchestrator
import org.springframework.stereotype.Service
import java.time.Year

@Service
class RefreshService(
    private val appStartupOrchestrator: AppStartupOrchestrator,
) {
    private val logger = loggerWithPrefix()

    suspend fun refreshCurrentSeason() {
        val currentYear = Year.now().value
        logger.info("🔁 Scheduled refresh for current season: $currentYear")

        appStartupOrchestrator.refreshChampion(currentYear)
        appStartupOrchestrator.refreshSeasons(currentYear)
    }
}
