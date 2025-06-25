package com.elkhami.f1champions.champions.infrastructure.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ApiConstructor(
    @JsonProperty("name") val name: String,
)
