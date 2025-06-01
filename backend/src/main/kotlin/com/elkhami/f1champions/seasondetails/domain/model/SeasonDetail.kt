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
) : Serializable
