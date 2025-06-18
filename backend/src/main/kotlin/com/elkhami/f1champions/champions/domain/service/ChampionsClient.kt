package com.elkhami.f1champions.champions.domain.service

import com.elkhami.f1champions.champions.domain.model.Champion

interface ChampionsClient {
    suspend fun fetchChampion(year: Int): Champion?
}
