package com.elkhami.f1champions.seasondetails.domain.model

import java.io.Serializable

data class SeasonDetail(
    val season: String,
    val round: String,
    val raceName: String,
    val date: String,
    val winnerId: String,
    val winnerName: String,
    val constructor: String,
) : Serializable {
    init {
        require(season.matches(Regex("\\d{4}"))) { "Invalid season format: must be 4 digits" }
        require(round.isNotBlank()) { "Round cannot be blank" }
        require(raceName.isNotBlank()) { "Race name cannot be blank" }
        require(date.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) { "Invalid date format: must be YYYY-MM-DD" }
        require(winnerId.isNotBlank()) { "Winner ID cannot be blank" }
        require(winnerName.isNotBlank()) { "Winner name cannot be blank" }
        require(constructor.isNotBlank()) { "Constructor cannot be blank" }
        require(season.toIntOrNull() in 1950..2050) { "Season must be between 1950 and 2050" }
    }
}
