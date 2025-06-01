package com.elkhami.f1champions.champions.domain

import com.elkhami.f1champions.core.network.Result

/**
 * Created by A.Elkhami on 22/05/2025.
 */
interface ChampionRepository {
    suspend fun getChampions(): Result<List<Champion>>
}
