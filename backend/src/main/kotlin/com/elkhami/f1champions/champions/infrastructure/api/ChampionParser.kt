package com.elkhami.f1champions.champions.infrastructure.api

import com.elkhami.f1champions.champions.domain.model.Champion
import com.fasterxml.jackson.databind.ObjectMapper

object ChampionParser {
    fun parseChampions(json: String?): List<Champion> {
        val root = ObjectMapper().readTree(json)
        return root["MRData"]["StandingsTable"]["StandingsLists"].map {
            val driver = it["DriverStandings"][0]["Driver"]
            val constructor = it["DriverStandings"][0]["Constructors"][0]
            Champion(
                season = it["season"].asText(),
                driverId = driver["driverId"].asText(),
                driverName = "${driver["givenName"].asText()} ${driver["familyName"].asText()}",
                constructor = constructor["name"].asText(),
            )
        }
    }
}
