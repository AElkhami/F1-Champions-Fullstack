package com.elkhami.f1champions.champions.data.repository

import com.elkhami.f1champions.champions.data.mapper.toChampion
import com.elkhami.f1champions.champions.data.remote.ChampionService
import com.elkhami.f1champions.champions.domain.Champion
import com.elkhami.f1champions.champions.domain.ChampionRepository
import com.elkhami.f1champions.core.network.Result
import com.elkhami.f1champions.core.network.safeCall
import javax.inject.Inject

/**
 * Created by A.Elkhami on 22/05/2025.
 */
class RemoteChampionRepository @Inject constructor(
    private val api: ChampionService
) : ChampionRepository {

    override suspend fun getChampions(): Result<List<Champion>> {
        return safeCall {
            api.getChampions().map { championDto -> championDto.toChampion() }
        }
    }
}
