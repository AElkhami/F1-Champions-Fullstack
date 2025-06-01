package com.elkhami.f1champions.seasondetails.data.repository

import com.elkhami.f1champions.core.network.Result
import com.elkhami.f1champions.core.network.safeCall
import com.elkhami.f1champions.seasondetails.data.mapper.toSeasonRaceResult
import com.elkhami.f1champions.seasondetails.data.remote.SeasonDetailsService
import com.elkhami.f1champions.seasondetails.domain.SeasonDetailsRepository
import com.elkhami.f1champions.seasondetails.domain.SeasonRaceResult
import javax.inject.Inject

/**
 * Created by A.Elkhami on 22/05/2025.
 */
class RemoteSeasonDetailsRepository @Inject constructor(
    private val api: SeasonDetailsService
) : SeasonDetailsRepository {

    override suspend fun getRaceResults(season: String): Result<List<SeasonRaceResult>> {
        return safeCall {
            api.getRaceResults(season).map { it.toSeasonRaceResult() }
        }
    }
}
