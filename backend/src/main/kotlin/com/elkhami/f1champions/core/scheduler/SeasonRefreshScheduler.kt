package com.elkhami.f1champions.core.scheduler

import com.elkhami.f1champions.core.logger.loggerWithPrefix
import com.elkhami.f1champions.core.seeding.SeedingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SeasonRefreshScheduler(
    private val seedingService: SeedingService,
    private val coroutineScope: CoroutineScope,
) {
    private val logger = loggerWithPrefix()

    // Every week (Monday at 5am UTC)
    @Scheduled(cron = "0 0 5 * * MON")
    fun scheduleWeeklyRefresh() {
        logger.info("📆 Triggering weekly season refresh job...")
        coroutineScope.launch {
            try {
                seedingService.refreshCurrentSeason()
            } catch (e: Exception) {
                logger.error("❌ Failed to refresh current season: ${e.message}")
            }
        }
    }
}
