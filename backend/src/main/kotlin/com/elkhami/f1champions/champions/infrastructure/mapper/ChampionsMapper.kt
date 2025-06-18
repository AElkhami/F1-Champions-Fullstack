package com.elkhami.f1champions.champions.infrastructure.mapper

import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.champions.infrastructure.db.entity.ChampionEntity
import java.util.UUID

fun Champion.toEntity(existingId: UUID? = null) =
    ChampionEntity(
        id = existingId ?: UUID.randomUUID(),
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
