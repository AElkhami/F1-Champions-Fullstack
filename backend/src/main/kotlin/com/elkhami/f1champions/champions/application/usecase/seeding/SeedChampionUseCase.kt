package com.elkhami.f1champions.champions.application.usecase.seeding

interface SeedChampionUseCase {
    suspend fun seedIfMissing(year: Int)

    suspend fun forceRefresh(year: Int)
}
