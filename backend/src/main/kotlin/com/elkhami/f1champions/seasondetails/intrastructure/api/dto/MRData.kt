package com.elkhami.f1champions.seasondetails.intrastructure.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class MRData(
    @JsonProperty("RaceTable") val raceTable: RaceTable,
)
