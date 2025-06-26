package com.elkhami.f1champions.seasondetails.intrastructure.api

import com.elkhami.f1champions.core.constants.ApiConstants.FIRST_POSITION
import com.elkhami.f1champions.core.logger.loggerWithPrefix
import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1champions.seasondetails.domain.service.SeasonDetailsParser
import com.elkhami.f1champions.seasondetails.intrastructure.api.dto.SeasonDetailsApiResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class F1SeasonDetailsParser(private val objectMapper: ObjectMapper) : SeasonDetailsParser {
    val logger = loggerWithPrefix()

    override fun parseSeasonDetails(
        season: String,
        json: String?,
    ): List<SeasonDetail> {
        if (json.isNullOrBlank()) {
            logger.warn("⚠️ Received null or blank JSON for season details: $season")
            return emptyList()
        }

        return try {
            val response = objectMapper.readValue(json, SeasonDetailsApiResponse::class.java)

            val races =
                response.mrData.raceTable.races

            races.mapNotNull { race ->
                val results =
                    race.results

                val result = results.getOrNull(FIRST_POSITION)
                val winner = result?.driver
                val constructor = result?.constructor

                if (winner == null || constructor == null) {
                    logger.warn("⚠️ Missing winner or constructor for season=$season round=${race.round}")
                    return@mapNotNull null
                }

                try {
                    SeasonDetail(
                        season = season,
                        round = race.round,
                        raceName = race.raceName,
                        date = race.date,
                        winnerId = winner.driverId,
                        winnerName = "${winner.givenName} ${winner.familyName}",
                        constructor = constructor.name,
                    )
                } catch (e: IllegalArgumentException) {
                    logger.warn("⚠️ Invalid season detail data for season=$season round=${race.round}: ${e.message}")
                    null
                }
            }
        } catch (ex: Exception) {
            logger.warn("⚠️ Failed to parse season details for $season: ${ex.message}")
            emptyList()
        }
    }
}
