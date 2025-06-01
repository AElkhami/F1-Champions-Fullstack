package com.elkhami.f1champions.seasondetails.intrastructure.api

import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import com.fasterxml.jackson.databind.ObjectMapper

object SeasonDetailsParser {
    fun parseSeasonDetails(
        season: String,
        json: String?,
    ): List<SeasonDetail> {
        val root = ObjectMapper().readTree(json)
        return root["MRData"]["RaceTable"]["Races"].map {
            val winner = it["Results"][0]["Driver"]
            val constructor = it["Results"][0]["Constructor"]
            SeasonDetail(
                season = season,
                round = it["round"].asText(),
                raceName = it["raceName"].asText(),
                date = it["date"].asText(),
                winnerId = winner["driverId"].asText(),
                winnerName = "${winner["givenName"].asText()} ${winner["familyName"].asText()}",
                constructor = constructor["name"].asText(),
            )
        }
    }
}
