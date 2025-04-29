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
import com.example.myfirstandroidtvapp.presentation.sections.tv_guide.TvGuideScreen
import com.example.myfirstandroidtvapp.presentation.shared_viewmodel.ChannelViewModel
import com.example.myfirstandroidtvapp.presentation.videoplayer.VideoPlayer

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val vodViewModel: VodViewModel = hiltViewModel()
    val channelViewModel: ChannelViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginRegistrationScreen(loginViewModel, vodViewModel, channelViewModel, navController) }
        composable("home") { HomeScreen(vodViewModel, loginViewModel,channelViewModel, navController) }
        composable("movies") { MovieScreen() }
        composable("dashboard") { DashBoardScreen(vodViewModel,navController) }
        composable("search") { SearchScreen() }
        composable("series") { SeriesScreen() }
        composable("tvguide") { TvGuideScreen(channelViewModel,navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("video_details") { MovieDetailScreen(vodViewModel,navController) }
        composable("video_player") { VideoPlayer(vodViewModel) }
    }
}
