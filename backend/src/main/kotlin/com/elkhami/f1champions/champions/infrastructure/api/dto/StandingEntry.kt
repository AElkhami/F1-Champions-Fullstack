package com.elkhami.f1champions.champions.infrastructure.api.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class StandingEntry(
    @JsonProperty("season") val season: String,
    @JsonProperty("DriverStandings") val driverStandings: List<DriverStanding>,
)
