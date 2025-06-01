package com.elkhami.f1champions.seasondetails.presentation

import com.elkhami.f1champions.core.network.AppError

/**
 * Created by A.Elkhami on 22/05/2025.
 */

data class SeasonDetailsUiState(
    val isLoading: Boolean = false,
    val races: List<RaceItemUiState> = emptyList(),
    val error: AppError? = null,
    val seasonTitle: String = ""
)

data class RaceItemUiState(
    val round: String,
    val raceName: String,
    val date: String,
    val winnerName: String,
    val constructorName: String
)
