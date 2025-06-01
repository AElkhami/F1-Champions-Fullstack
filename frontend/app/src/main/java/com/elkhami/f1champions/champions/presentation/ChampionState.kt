package com.elkhami.f1champions.champions.presentation

import com.elkhami.f1champions.core.network.AppError

/**
 * Created by A.Elkhami on 22/05/2025.
 */
data class ChampionsUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val champions: List<ChampionItemUiState> = emptyList(),
    val error: AppError? = null
)

data class ChampionItemUiState(
    val title: String,
    val driver: String,
    val constructor: String,
    val season: String
)

