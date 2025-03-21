package com.example.myfirstandroidtvapp.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myfirstandroidtvapp.presentation.detailscreen.DetailScreen
import com.example.myfirstandroidtvapp.presentation.login.LoginRegistrationScreen
import com.example.myfirstandroidtvapp.presentation.login.LoginScreen
import com.example.myfirstandroidtvapp.presentation.sections.home.HomeScreen
import com.example.myfirstandroidtvapp.presentation.sections.home.HomeViewModel
import com.example.myfirstandroidtvapp.presentation.sections.movies.MovieScreen
import com.example.myfirstandroidtvapp.presentation.sections.search.SearchScreen
import com.example.myfirstandroidtvapp.presentation.sections.series.SeriesScreen
import com.example.myfirstandroidtvapp.presentation.sections.settings.SettingsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginRegistrationScreen(navController) }
        composable("home") { HomeScreen(navController, homeViewModel) }
        composable("movies") { MovieScreen(homeViewModel) }
        composable("search") { SearchScreen(homeViewModel) }
        composable("series") { SeriesScreen(homeViewModel) }
        composable("settings") { SettingsScreen(homeViewModel) }
        composable("details/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            DetailScreen(movieId)
        }
    }
}
