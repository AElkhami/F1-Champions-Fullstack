package com.elkhami.f1champions.seasondetails.domain.service

import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail

interface SeasonDetailsClient {
    suspend fun fetchSeasonDetails(season: String): List<SeasonDetail>?
}
