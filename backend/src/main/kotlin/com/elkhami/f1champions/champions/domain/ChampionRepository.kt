package com.elkhami.f1champions.champions.domain

import com.elkhami.f1champions.champions.infrastructure.db.entity.ChampionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ChampionRepository : JpaRepository<ChampionEntity, UUID> {
    fun findBySeason(season: String): ChampionEntity?
}
