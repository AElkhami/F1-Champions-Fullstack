package com.elkhami.f1champions.seasondetails.application.seeding

import com.elkhami.f1champions.seasondetails.domain.service.SeasonDetailsService
import com.elkhami.f1champions.seasondetails.intrastructure.api.SeasonDetailsClient
import com.elkhami.f1champions.seasondetails.intrastructure.mapper.toEntity
import org.springframework.stereotype.Component

@Component
class F1SeasonDetailsSeeder(
    private val seasonDetailsClient: SeasonDetailsClient,
    private val seasonDetailsService: SeasonDetailsService,
) : SeasonDetailsSeeder {
    override suspend fun seedIfMissing(year: Int) {
        val season = year.toString()
        if (seasonDetailsExist(season)) return

        val winners = seasonDetailsClient.fetchSeasonDetails(season)
        winners.forEach { seasonDetailsService.saveSeasonDetails(it.toEntity()) }
    }

    private suspend fun seasonDetailsExist(season: String): Boolean {
        return seasonDetailsService.findDetailsBySeason(season).isNotEmpty()
    }
}
