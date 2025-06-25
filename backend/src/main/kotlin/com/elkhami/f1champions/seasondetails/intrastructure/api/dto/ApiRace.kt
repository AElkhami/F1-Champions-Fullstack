package com.elkhami.f1champions.seasondetails.intrastructure.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ApiRace(
    @JsonProperty("season") val season: String,
    @JsonProperty("round") val round: String,
    @JsonProperty("raceName") val raceName: String,
    @JsonProperty("date") val date: String,
    @JsonProperty("Results") val results: List<ApiResult>,
)
