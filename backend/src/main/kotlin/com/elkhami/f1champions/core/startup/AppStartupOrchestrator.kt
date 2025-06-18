package com.elkhami.f1champions.core.startup

import com.elkhami.f1champions.champions.application.usecase.seeding.SeedChampionUseCase
import com.elkhami.f1champions.core.logger.loggerWithPrefix
import com.elkhami.f1champions.seasondetails.application.usecase.seeding.SeedSeasonDetailsUseCase
import org.springframework.stereotype.Component

@Component
class AppStartupOrchestrator(
    private val seedChampionUseCase: SeedChampionUseCase,
    private val seedSeasonDetailsUseCase: SeedSeasonDetailsUseCase,
) {
    private val logger = loggerWithPrefix()

    suspend fun startUpSeed(
        fromYear: Int = 2005,
        toYear: Int = 2025,
    ) {
        for (year in fromYear..toYear) {
            seedChampions(year)
            seedSeasons(year)
        }
    }

    suspend fun seedChampions(year: Int) {
        runCatching {
            seedChampionUseCase.seedIfMissing(year)
        }.onFailure {
            logger.error("❌ Error seeding champion for year $year, ${it.message}")
        }
    }

    suspend fun seedSeasons(year: Int) {
        runCatching {
            seedSeasonDetailsUseCase.seedIfMissing(year)
        }.onFailure {
            logger.error("❌ Error seeding season details for year $year, ${it.message}")
        }
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
