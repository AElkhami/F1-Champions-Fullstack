package com.elkhami.f1champions.champions.infrastructure.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class StandingEntry(
    @JsonProperty("season") val season: String,
    @JsonProperty("DriverStandings") val driverStandings: List<DriverStanding>,
)
