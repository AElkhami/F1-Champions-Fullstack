package com.elkhami.f1champions.seasondetails.domain.service

import com.elkhami.f1champions.core.network.ApiResponse
import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail

interface SeasonDetailsClient {
    suspend fun fetchSeasonDetails(season: String): ApiResponse<List<SeasonDetail>?>
}
