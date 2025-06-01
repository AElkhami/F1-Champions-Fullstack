package com.elkhami.f1champions.seasondetails.domain.service

import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1champions.seasondetails.intrastructure.db.entity.SeasonDetailsEntity

interface SeasonDetailsService {
    fun getSeasonDetails(season: String): List<SeasonDetail>

    fun findDetailsBySeason(season: String): List<SeasonDetailsEntity>

    fun saveSeasonDetails(seasonDetailsEntity: SeasonDetailsEntity)
}
