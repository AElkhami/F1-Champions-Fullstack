package com.elkhami.f1champions.champions.domain.service

import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.champions.infrastructure.db.entity.ChampionEntity

interface ChampionsService {
    fun getChampions(): List<Champion>

    fun findChampionsBySeason(season: String): ChampionEntity?

    fun saveChampion(championEntity: ChampionEntity)
}
