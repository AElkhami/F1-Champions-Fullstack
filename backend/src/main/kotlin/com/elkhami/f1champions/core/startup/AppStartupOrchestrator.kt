package com.elkhami.f1champions.core.startup

import com.elkhami.f1champions.champions.application.seeding.ChampionSeeder
import com.elkhami.f1champions.core.logger.loggerWithPrefix
import com.elkhami.f1champions.seasondetails.application.seeding.SeasonDetailsSeeder
import org.springframework.stereotype.Component

@Component
class AppStartupOrchestrator(
    private val championSeeder: ChampionSeeder,
    private val seasonDetailsSeeder: SeasonDetailsSeeder,
) {
    private val logger = loggerWithPrefix()

    suspend fun seed(
        fromYear: Int = 2005,
        toYear: Int = 2025,
    ) {
        for (year in fromYear..toYear) {
            runCatching {
                championSeeder.seedIfMissing(year)
            }.onFailure {
                logger.error("❌ Error seeding champion for year $year")
            }

            runCatching {
                seasonDetailsSeeder.seedIfMissing(year)
            }.onFailure {
                logger.error("❌ Error seeding season details for year $year")
            }
        }
    }
}
