package com.elkhami.f1champions.seasondetails.data.mapper

import com.elkhami.f1champions.seasondetails.data.model.SeasonDetailsDto
import com.elkhami.f1champions.seasondetails.domain.SeasonRaceResult

/**
 * Created by A.Elkhami on 22/05/2025.
 */

fun SeasonDetailsDto.toSeasonRaceResult(): SeasonRaceResult {
    return SeasonRaceResult(
        round = round ?: "-",
        raceName = raceName ?: "Unknown Race",
        date = date ?: "Unknown Date",
        winner = winnerName ?: "Unknown Winner",
        constructor = constructor ?: "Unknown Constructor"
    )
}

