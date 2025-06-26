package com.elkhami.f1champions.champions.domain.service

import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.core.network.ApiResponse

interface ChampionsClient {
    suspend fun fetchChampion(year: Int): ApiResponse<Champion?>
}
