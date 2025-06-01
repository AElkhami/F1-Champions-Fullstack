package com.elkhami.f1champions.seasondetails.data.remote

import com.elkhami.f1champions.seasondetails.data.model.SeasonDetailsDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by A.Elkhami on 22/05/2025.
 */

interface SeasonDetailsService {
    @GET("f1/champions/{season}")
    suspend fun getRaceResults(@Path("season") season: String): List<SeasonDetailsDto>
}
