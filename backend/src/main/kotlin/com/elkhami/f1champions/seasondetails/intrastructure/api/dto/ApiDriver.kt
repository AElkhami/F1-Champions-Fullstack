package com.elkhami.f1champions.seasondetails.intrastructure.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ApiDriver(
    @JsonProperty("driverId") val driverId: String,
    @JsonProperty("givenName") val givenName: String,
    @JsonProperty("familyName") val familyName: String,
)
