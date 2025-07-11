package com.elkhami.f1champions.champions.infrastructure.api

import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.champions.domain.service.ChampionParser
import com.elkhami.f1champions.champions.infrastructure.api.dto.ChampionApiResponse
import com.elkhami.f1champions.core.constants.ApiConstants.FIRST_POSITION
import com.elkhami.f1champions.core.logger.loggerWithPrefix
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class F1ChampionParser(private val objectMapper: ObjectMapper) : ChampionParser {
    override fun parseChampions(json: String?): List<Champion> {
        val logger = loggerWithPrefix()

        if (json.isNullOrBlank()) {
            logger.warn("⚠️ Received null or blank JSON for champions")
            return emptyList()
        }

        return try {
            val response = objectMapper.readValue(json, ChampionApiResponse::class.java)

            val standingsLists =
                response.mrData.standingsTable.standingsLists

            standingsLists.mapNotNull { standing ->
                val driverStandings =
                    standing.driverStandings

                val driverStanding = driverStandings.getOrNull(FIRST_POSITION)
                val driver = driverStanding?.driver
                val constructors = driverStanding?.constructors
                val constructor = constructors?.getOrNull(FIRST_POSITION)

                if (driver == null || constructor == null) {
                    logger.warn("⚠️ Missing driver or constructor for season=${standing.season}")
                    return@mapNotNull null
                }

                try {
                    Champion(
                        season = standing.season,
                        driverId = driver.driverId,
                        driverName = "${driver.givenName} ${driver.familyName}",
                        constructor = constructor.name,
                    )
                } catch (e: IllegalArgumentException) {
                    logger.warn("⚠️ Invalid champion data for season=${standing.season}: ${e.message}")
                    null
                }
            }
        } catch (ex: Exception) {
            logger.warn("⚠️ Failed to parse champions: ${ex.message}")
            emptyList()
        }
    }
}
