package com.example.myfirstandroidtvapp.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myfirstandroidtvapp.presentation.detailscreen.MovieDetailScreen
import com.example.myfirstandroidtvapp.presentation.login.LoginRegistrationScreen
import com.example.myfirstandroidtvapp.presentation.login.LoginViewModel
import com.example.myfirstandroidtvapp.presentation.sections.dashboard.DashBoardScreen
import com.example.myfirstandroidtvapp.presentation.sections.dashboard.VodViewModel
import com.example.myfirstandroidtvapp.presentation.sections.home.HomeScreen
import com.example.myfirstandroidtvapp.presentation.sections.movies.MovieScreen
import com.example.myfirstandroidtvapp.presentation.sections.search.SearchScreen
import com.example.myfirstandroidtvapp.presentation.sections.series.SeriesScreen
import com.example.myfirstandroidtvapp.presentation.sections.settings.SettingsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val vodViewModel: VodViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginRegistrationScreen(loginViewModel, navController) }
        composable("home") { HomeScreen(vodViewModel, loginViewModel, navController) }
        composable("movies") { MovieScreen() }
        composable("dashboard") { DashBoardScreen(vodViewModel,navController) }
        composable("search") { SearchScreen() }
        composable("series") { SeriesScreen() }
        composable("settings") { SettingsScreen() }
        composable("video_details") { MovieDetailScreen(vodViewModel) }
    }
}
