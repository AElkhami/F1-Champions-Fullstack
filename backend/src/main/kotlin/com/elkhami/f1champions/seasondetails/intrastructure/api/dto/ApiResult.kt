package com.elkhami.f1champions.seasondetails.intrastructure.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ApiResult(
    @JsonProperty("Driver") val driver: ApiDriver,
    @JsonProperty("Constructor") val constructor: ApiConstructor,
)
