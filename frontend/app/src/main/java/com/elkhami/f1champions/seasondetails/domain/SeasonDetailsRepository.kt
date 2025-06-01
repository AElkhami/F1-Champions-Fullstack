package com.elkhami.f1champions.seasondetails.domain

import com.elkhami.f1champions.core.network.Result

/**
 * Created by A.Elkhami on 22/05/2025.
 */
interface SeasonDetailsRepository {
    suspend fun getRaceResults(season: String): Result<List<SeasonRaceResult>>
}