package com.elkhami.f1champions.champions.application

import com.elkhami.f1champions.champions.domain.ChampionRepository
import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.champions.domain.service.ChampionsService
import com.elkhami.f1champions.champions.infrastructure.db.entity.ChampionEntity
import com.elkhami.f1champions.champions.infrastructure.mapper.toDomain
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class F1ChampionsService(
    private val championRepo: ChampionRepository,
) : ChampionsService {
    @Cacheable("champions")
    override fun getChampions(): List<Champion> {
        return championRepo.findAll().map { it.toDomain() }
    }

    override fun findChampionsBySeason(season: String): ChampionEntity? {
        return championRepo.findBySeason(season)
    }

    override fun saveChampion(championEntity: ChampionEntity) {
        championRepo.save(championEntity)
    }
}
