package com.elkhami.f1champions.navigation

/**
 * Created by A.Elkhami on 22/05/2025.
 */

sealed class Screen(val route: String) {
    data object Champions : Screen("champions")
    data object SeasonDetails : Screen("season_details/{season}/{championName}") {
        fun createRoute(season: String, championName: String) = "season_details/$season/$championName"
    }
}
