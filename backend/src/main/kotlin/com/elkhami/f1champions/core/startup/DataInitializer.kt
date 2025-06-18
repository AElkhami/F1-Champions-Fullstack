package com.elkhami.f1champions.core.startup

import com.elkhami.f1champions.core.logger.loggerWithPrefix
import jakarta.annotation.PreDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val appStartupOrchestrator: AppStartupOrchestrator,
    @Qualifier("appCoroutineScope")
    private val scope: CoroutineScope,
) {
    private val logger = loggerWithPrefix()

    @EventListener(ApplicationReadyEvent::class)
    fun onAppReady() {
        logger.info("🚀 App started. Beginning data seeding...")
        scope.launch {
            try {
                appStartupOrchestrator.startUpSeed()
            } catch (e: Exception) {
                logger.error("❌ Failed during seeding: ${e.message}")
            }
        }
    }

    @PreDestroy
    fun onShutdown() {
        logger.info("🧹 Shutting down CoroutineScope...")
        scope.cancel()
    }
}
