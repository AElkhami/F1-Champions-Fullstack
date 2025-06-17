package com.elkhami.f1champions.seasondetails.application.usecase.seeding

interface SeedSeasonDetailsUseCase {
    suspend fun seedIfMissing(year: Int)

    suspend fun forceRefresh(year: Int)
}
