package com.elkhami.f1champions.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.elkhami.f1champions.champions.presentation.ChampionsScreen
import com.elkhami.f1champions.seasondetails.presentation.SeasonDetailsScreen

/**
 * Created by A.Elkhami on 22/05/2025.
 */

@Composable
fun F1AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Champions.route
    ) {
        composable(Screen.Champions.route) {
            ChampionsScreen(
                onSeasonClick = { season, championName ->
                    navController.navigate(Screen.SeasonDetails.createRoute(season, championName))
                }
            )
        }

        composable(
            route = Screen.SeasonDetails.route,
            arguments = listOf(navArgument("season") { type = NavType.StringType })
        ) { entry ->
            val season = entry.arguments?.getString("season") ?: return@composable
            val championName = entry.arguments?.getString("championName") ?: return@composable
            SeasonDetailsScreen(
                season = season,
                championName = championName,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
