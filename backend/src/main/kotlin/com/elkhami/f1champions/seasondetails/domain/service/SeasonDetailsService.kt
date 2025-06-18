package com.elkhami.f1champions.seasondetails.domain.service

import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail

interface SeasonDetailsService {
    fun findDetailsBySeason(season: String): List<SeasonDetail>?

    fun saveSeasonDetails(seasonDetail: SeasonDetail)

    fun evictSeasonCache(season: String)
}
