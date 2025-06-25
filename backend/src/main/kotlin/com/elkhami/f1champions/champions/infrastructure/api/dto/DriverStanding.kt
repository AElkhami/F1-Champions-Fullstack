package com.elkhami.f1champions.champions.infrastructure.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class DriverStanding(
    @JsonProperty("Driver") val driver: ApiDriver?,
    @JsonProperty("Constructors") val constructors: List<ApiConstructor>,
)
