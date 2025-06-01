package com.elkhami.f1champions.champions.domain.model

import java.io.Serializable

data class Champion(
    val season: String,
    val driverId: String,
    val driverName: String,
    val constructor: String,
) : Serializable
