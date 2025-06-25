package com.elkhami.f1champions.champions.infrastructure.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ChampionApiResponse(
    @JsonProperty("MRData") val mrData: ChampionMRData,
)
