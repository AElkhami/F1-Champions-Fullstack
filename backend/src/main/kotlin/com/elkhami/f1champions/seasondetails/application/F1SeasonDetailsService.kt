package com.elkhami.f1champions.seasondetails.application

import com.elkhami.f1champions.seasondetails.domain.SeasonDetailsRepository
import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1champions.seasondetails.domain.service.SeasonDetailsService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class F1SeasonDetailsService(
    private val seasonDetailsRepository: SeasonDetailsRepository,
) : SeasonDetailsService {
    override fun findDetailsBySeason(season: String): List<SeasonDetail>? {
        return seasonDetailsRepository.findBySeason(season)
    }

    @Transactional
    override fun saveSeasonDetails(seasonDetail: SeasonDetail) {
        seasonDetailsRepository.deleteBySeasonAndRound(
            seasonDetail.season,
            seasonDetail.round,
        )

        seasonDetailsRepository.save(seasonDetail)
    }

    override fun evictSeasonCache(season: String) {
        seasonDetailsRepository.evictSeasonCache(season)
    }
}
