package com.elkhami.f1champions.core.startup

import com.elkhami.f1champions.champions.application.usecase.seeding.SeedChampionUseCase
import com.elkhami.f1champions.core.constants.ApiConstants.DEFAULT_SEASON_RANGE
import com.elkhami.f1champions.core.logger.loggerWithPrefix
import com.elkhami.f1champions.seasondetails.application.usecase.seeding.SeedSeasonDetailsUseCase
import kotlinx.coroutines.delay
import org.springframework.stereotype.Component

@Component
class AppStartupOrchestrator(
    private val seedChampionUseCase: SeedChampionUseCase,
    private val seedSeasonDetailsUseCase: SeedSeasonDetailsUseCase,
) {
    private val logger = loggerWithPrefix()

    suspend fun startUpSeed(
        fromYear: Int = DEFAULT_SEASON_RANGE.first,
        toYear: Int = DEFAULT_SEASON_RANGE.last,
    ) {
        logger.info("🚀 Starting data seeding from $fromYear to $toYear")
        var successCount = 0
        var failureCount = 0

        for (year in fromYear..toYear) {
            val championSuccess = seedChampionsWithRetry(year)
            val seasonSuccess = seedSeasonsWithRetry(year)

            if (championSuccess && seasonSuccess) {
                successCount++
            } else {
                failureCount++
            }
        }

        logger.info("✅ Seeding completed: $successCount successful, $failureCount failed")
    }

    private suspend fun seedChampionsWithRetry(
        year: Int,
        maxRetries: Int = 3,
    ): Boolean {
        repeat(maxRetries) { attempt ->
            try {
                seedChampionUseCase.seedIfMissing(year)
                return true
            } catch (e: Exception) {
                logger.warn("⚠️ Attempt ${attempt + 1} failed for champion year $year: ${e.message}")
                if (attempt < maxRetries - 1) {
                    val delayMs = (1000L * (attempt + 1)) // Exponential backoff: 1s, 2s, 3s
                    delay(delayMs)
                }
            }
        }
        logger.error("❌ Failed to seed champion for year $year after $maxRetries attempts")
        return false
    }

    private suspend fun seedSeasonsWithRetry(
        year: Int,
        maxRetries: Int = 3,
    ): Boolean {
        repeat(maxRetries) { attempt ->
            try {
                seedSeasonDetailsUseCase.seedIfMissing(year)
                return true
            } catch (e: Exception) {
                logger.warn("⚠️ Attempt ${attempt + 1} failed for season details year $year: ${e.message}")
                if (attempt < maxRetries - 1) {
                    val delayMs = (1000L * (attempt + 1)) // Exponential backoff: 1s, 2s, 3s
                    delay(delayMs)
                }
            }
        }
        logger.error("❌ Failed to seed season details for year $year after $maxRetries attempts")
        return false
    }

    private suspend fun seedChampionsWithRetry(
        year: Int,
        maxRetries: Int = 3,
    ): Boolean {
        repeat(maxRetries) { attempt ->
            try {
                seedChampionUseCase.seedIfMissing(year)
                return true
            } catch (e: Exception) {
                logger.warn("⚠️ Attempt ${attempt + 1} failed for champion year $year: ${e.message}")
                if (attempt < maxRetries - 1) {
                    val delayMs = (1000L * (attempt + 1)) // Exponential backoff: 1s, 2s, 3s
                    delay(delayMs)
                }
            }
        }
        logger.error("❌ Failed to seed champion for year $year after $maxRetries attempts")
        return false
    }

    private suspend fun seedSeasonsWithRetry(
        year: Int,
        maxRetries: Int = 3,
    ): Boolean {
        repeat(maxRetries) { attempt ->
            try {
                seedSeasonDetailsUseCase.seedIfMissing(year)
                return true
            } catch (e: Exception) {
                logger.warn("⚠️ Attempt ${attempt + 1} failed for season details year $year: ${e.message}")
                if (attempt < maxRetries - 1) {
                    val delayMs = (1000L * (attempt + 1)) // Exponential backoff: 1s, 2s, 3s
                    delay(delayMs)
                }
            }
        }
        logger.error("❌ Failed to seed season details for year $year after $maxRetries attempts")
        return false
    }

    suspend fun refreshChampion(year: Int) {
        runCatching {
            seedChampionUseCase.forceRefresh(year)
        }.onFailure {
            logger.error("❌ Error refreshing champion for year $year, ${it.message}")
        }
    }

    suspend fun refreshSeasons(year: Int) {
        runCatching {
            seedSeasonDetailsUseCase.forceRefresh(year)
        }.onFailure {
            logger.error("❌ Error refreshing season details for year $year, ${it.message}")
        }
    }
}
