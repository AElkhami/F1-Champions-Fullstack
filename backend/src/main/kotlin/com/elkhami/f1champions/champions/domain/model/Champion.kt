package com.elkhami.f1champions.champions.domain.model

import java.io.Serializable

data class Champion(
    val season: String,
    val driverId: String,
    val driverName: String,
    val constructor: String,
) : Serializable {
    init {
        require(season.matches(Regex("\\d{4}"))) { "Invalid season format: must be 4 digits" }
        require(driverId.isNotBlank()) { "Driver ID cannot be blank" }
        require(driverName.isNotBlank()) { "Driver name cannot be blank" }
        require(constructor.isNotBlank()) { "Constructor cannot be blank" }
        require(season.toIntOrNull() in 1950..2050) { "Season must be between 1950 and 2050" }
    }
}
