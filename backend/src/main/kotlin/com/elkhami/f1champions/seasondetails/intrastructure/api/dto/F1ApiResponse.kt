package com.elkhami.f1champions.seasondetails.intrastructure.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class F1ApiResponse(
    @JsonProperty("MRData") val mrData: MRData,
)
