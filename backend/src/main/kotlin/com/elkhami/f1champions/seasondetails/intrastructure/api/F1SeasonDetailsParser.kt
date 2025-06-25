package com.elkhami.f1champions.seasondetails.intrastructure.api

import com.elkhami.f1champions.core.logger.loggerWithPrefix
import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1champions.seasondetails.domain.service.SeasonDetailsParser
import com.elkhami.f1champions.seasondetails.intrastructure.api.dto.F1ApiResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class F1SeasonDetailsParser(private val objectMapper: ObjectMapper) : SeasonDetailsParser {
    val logger = loggerWithPrefix()

    override fun parseSeasonDetails(
        season: String,
        json: String?,
    ): List<SeasonDetail> {
        return try {
            val response = objectMapper.readValue(json, F1ApiResponse::class.java)
            response.mrData.raceTable.races.mapNotNull { race ->
                val result = race.results.getOrNull(0)
                val winner = result?.driver
                val constructor = result?.constructor

                if (winner == null || constructor == null) {
                    logger.warn("⚠️ Missing winner or constructor for season=$season round=${race.round}")
                    return@mapNotNull null
                }

                SeasonDetail(
                    season = season,
                    round = race.round,
                    raceName = race.raceName,
                    date = race.date,
                    winnerId = winner.driverId,
                    winnerName = "${winner.givenName} ${winner.familyName}",
                    constructor = constructor.name,
                )
            }
        } catch (ex: Exception) {
            logger.warn("⚠️ Failed to parse season details for $season: ${ex.message}")
            emptyList()
        }
    }
}
