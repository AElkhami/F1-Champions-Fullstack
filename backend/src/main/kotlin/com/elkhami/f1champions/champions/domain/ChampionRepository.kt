package com.elkhami.f1champions.champions.domain

import com.elkhami.f1champions.champions.domain.model.Champion

interface ChampionRepository {
    fun findAll(): List<Champion>

    fun findBySeason(season: String): Champion?

    fun save(champion: Champion): Champion

    fun evictSeasonCache(season: String)
}
