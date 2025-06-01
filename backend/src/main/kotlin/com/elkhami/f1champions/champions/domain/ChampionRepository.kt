package com.elkhami.f1champions.champions.domain

import com.elkhami.f1champions.champions.infrastructure.db.entity.ChampionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ChampionRepository : JpaRepository<ChampionEntity, Long> {
    fun findBySeason(season: String): ChampionEntity?
}
