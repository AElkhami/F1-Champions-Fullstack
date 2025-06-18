package com.elkhami.f1champions.seasondetails.intrastructure.repository

import com.elkhami.f1champions.seasondetails.intrastructure.db.entity.SeasonDetailsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

interface SeasonDetailsJpaRepository : JpaRepository<SeasonDetailsEntity, UUID> {
    fun findBySeason(season: String): List<SeasonDetailsEntity>?

    @Transactional
    fun deleteBySeasonAndRound(
        season: String,
        round: String,
    )
}
