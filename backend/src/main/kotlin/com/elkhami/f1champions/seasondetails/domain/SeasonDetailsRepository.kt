package com.elkhami.f1champions.seasondetails.domain

import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail

interface SeasonDetailsRepository {
    fun findBySeason(season: String): List<SeasonDetail>?

    fun deleteBySeasonAndRound(
        season: String,
        round: String,
    )

    fun save(seasonDetail: SeasonDetail): SeasonDetail

    fun evictSeasonCache(season: String)
}
