package com.elkhami.f1champions.champions.infrastructure.mapper

import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.champions.infrastructure.db.entity.ChampionEntity

fun Champion.toEntity() =
    ChampionEntity(
        season = this.season,
        driverId = this.driverId,
        driverName = this.driverName,
        constructor = this.constructor,
    )

fun ChampionEntity.toDomain() =
    Champion(
        season = this.season,
        driverId = this.driverId,
        driverName = this.driverName,
        constructor = this.constructor,
    )
