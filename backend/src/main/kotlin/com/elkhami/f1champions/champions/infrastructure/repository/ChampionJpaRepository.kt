package com.elkhami.f1champions.champions.infrastructure.repository

import com.elkhami.f1champions.champions.infrastructure.db.entity.ChampionEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ChampionJpaRepository : JpaRepository<ChampionEntity, UUID> {
    fun findBySeason(season: String): ChampionEntity?
}
