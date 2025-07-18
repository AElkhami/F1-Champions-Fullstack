package com.elkhami.f1champions.champions.application

import com.elkhami.f1champions.champions.domain.ChampionRepository
import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.champions.domain.service.ChampionsService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class F1ChampionsService(
    private val championRepository: ChampionRepository,
) : ChampionsService {
    override fun getChampions(): List<Champion> {
        return championRepository.findAll()
    }

    override fun findChampionsBySeason(season: String): Champion? {
        return championRepository.findBySeason(season)
    }

    @Transactional
    override fun saveChampion(champion: Champion) {
        val existing = championRepository.findBySeason(champion.season)
        val championToSave =
            existing?.copy(
                driverId = champion.driverId,
                driverName = champion.driverName,
                constructor = champion.constructor,
            ) ?: champion

        championRepository.save(championToSave)

        if (existing != null) {
            championRepository.evictSeasonCache(champion.season)
        }
    }
}
