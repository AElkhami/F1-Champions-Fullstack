package com.elkhami.f1champions.seasondetails.intrastructure.api.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SeasonDetailsApiResponse(
    @JsonProperty("MRData") val mrData: MRData,
)
