package com.elkhami.f1champions.champions.data.mapper

import com.elkhami.f1champions.champions.data.model.ChampionDto
import com.elkhami.f1champions.champions.domain.Champion

/**
 * Created by A.Elkhami on 22/05/2025.
 */

fun ChampionDto.toChampion(): Champion = Champion(
    season = this.season,
    driverName = this.driverName,
    constructor = this.constructor
)
