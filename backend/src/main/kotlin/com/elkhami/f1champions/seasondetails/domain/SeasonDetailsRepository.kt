package com.elkhami.f1champions.seasondetails.domain

import com.elkhami.f1champions.seasondetails.intrastructure.db.entity.SeasonDetailsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
interface SeasonDetailsRepository : JpaRepository<SeasonDetailsEntity, UUID> {
    fun findBySeason(season: String): List<SeasonDetailsEntity>

    @Transactional
    fun deleteBySeasonAndRound(
        season: String,
        round: String,
    )
}
