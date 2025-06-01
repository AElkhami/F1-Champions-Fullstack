package com.elkhami.f1champions.seasondetails.application.seeding

interface SeasonDetailsSeeder {
    suspend fun seedIfMissing(year: Int)
}
