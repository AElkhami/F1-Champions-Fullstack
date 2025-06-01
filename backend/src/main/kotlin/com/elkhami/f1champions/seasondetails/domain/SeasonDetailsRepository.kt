package com.elkhami.f1champions.seasondetails.domain

import com.elkhami.f1champions.seasondetails.intrastructure.db.entity.SeasonDetailsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SeasonDetailsRepository : JpaRepository<SeasonDetailsEntity, Long> {
    fun findBySeason(season: String): List<SeasonDetailsEntity>
}
