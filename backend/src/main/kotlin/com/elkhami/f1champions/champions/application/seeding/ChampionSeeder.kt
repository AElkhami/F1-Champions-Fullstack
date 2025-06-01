package com.elkhami.f1champions.champions.application.seeding

interface ChampionSeeder {
    suspend fun seedIfMissing(year: Int)
}
