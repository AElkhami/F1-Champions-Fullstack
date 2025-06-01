package com.elkhami.f1champions.champions.data.remote

import com.elkhami.f1champions.champions.data.model.ChampionDto
import retrofit2.http.GET

/**
 * Created by A.Elkhami on 22/05/2025.
 */
interface ChampionService {
    @GET("f1/champions")
    suspend fun getChampions(): List<ChampionDto>
}
