package com.elkhami.f1champions.core.startup

import com.elkhami.f1champions.core.logger.loggerWithPrefix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val appStartupOrchestrator: AppStartupOrchestrator,
    private val scope: CoroutineScope,
) {
    private val logger = loggerWithPrefix()

    @EventListener(ApplicationReadyEvent::class)
    fun onAppReady() {
        logger.info("🚀 App started. Beginning data seeding...")
        scope.launch {
            try {
                appStartupOrchestrator.seed()
            } catch (e: Exception) {
                logger.error("❌ Failed during seeding: ${e.message}")
            }
        }
    }
}
