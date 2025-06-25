package com.elkhami.f1champions.champions.infrastructure.api

import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.champions.domain.service.ChampionParser
import com.elkhami.f1champions.champions.infrastructure.api.dto.ChampionApiResponse
import com.elkhami.f1champions.core.logger.loggerWithPrefix
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class F1ChampionParser(private val objectMapper: ObjectMapper) : ChampionParser {
    override fun parseChampions(json: String?): List<Champion> {
        val logger = loggerWithPrefix()

        return try {
            val response = objectMapper.readValue(json, ChampionApiResponse::class.java)

            response.mrData.standingsTable.standingsLists.mapNotNull { standing ->
                val driverStanding = standing.driverStandings.getOrNull(0)
                val driver = driverStanding?.driver
                val constructor = driverStanding?.constructors?.getOrNull(0)

                if (driver == null || constructor == null) {
                    logger.warn("⚠️ Missing driver or constructor for season=${standing.season}")
                    return@mapNotNull null
                }

                Champion(
                    season = standing.season,
                    driverId = driver.driverId,
                    driverName = "${driver.givenName} ${driver.familyName}",
                    constructor = constructor.name,
                )
            }
        } catch (ex: Exception) {
            logger.warn("⚠️ Failed to parse champions: ${ex.message}")
            emptyList()
        }
    }
}
