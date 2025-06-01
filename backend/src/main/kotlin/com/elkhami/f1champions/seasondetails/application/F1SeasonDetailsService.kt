package com.elkhami.f1champions.seasondetails.application

import com.elkhami.f1champions.seasondetails.domain.SeasonDetailsRepository
import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1champions.seasondetails.domain.service.SeasonDetailsService
import com.elkhami.f1champions.seasondetails.intrastructure.db.entity.SeasonDetailsEntity
import com.elkhami.f1champions.seasondetails.intrastructure.mapper.toDomain
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class F1SeasonDetailsService(
    private val seasonDetailsRepository: SeasonDetailsRepository,
) : SeasonDetailsService {
    @Cacheable("seasonDetails")
    override fun getSeasonDetails(season: String): List<SeasonDetail> {
        return seasonDetailsRepository.findBySeason(season).map { it.toDomain() }
    }

    override fun findDetailsBySeason(season: String): List<SeasonDetailsEntity> {
        return seasonDetailsRepository.findBySeason(season)
    }

    override fun saveSeasonDetails(seasonDetailsEntity: SeasonDetailsEntity) {
        seasonDetailsRepository.save(seasonDetailsEntity)
    }
}
