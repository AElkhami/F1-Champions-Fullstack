package com.elkhami.f1champions.seasondetails.domain.service

import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail

interface SeasonDetailsParser {
    fun parseSeasonDetails(
        season: String,
        json: String?,
    ): List<SeasonDetail>
}
