package com.elkhami.f1champions.champions.infrastructure.api.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ChampionApiResponse(
    @JsonProperty("MRData") val mrData: ChampionMRData,
)
