package com.elkhami.f1champions.seasondetails.intrastructure.mapper

import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1champions.seasondetails.intrastructure.db.entity.SeasonDetailsEntity

fun SeasonDetail.toEntity() =
    SeasonDetailsEntity(
        season = this.season,
        round = this.round,
        raceName = this.raceName,
        date = this.date,
        winnerId = this.winnerId,
        winnerName = this.winnerName,
        constructor = this.constructor,
    )

fun SeasonDetailsEntity.toDomain() =
    SeasonDetail(
        season = this.season,
        round = this.round,
        raceName = this.raceName,
        date = this.date,
        winnerId = this.winnerId,
        winnerName = this.winnerName,
        constructor = this.constructor,
    )
