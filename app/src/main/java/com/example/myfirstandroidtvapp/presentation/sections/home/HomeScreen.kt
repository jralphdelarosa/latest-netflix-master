package com.example.myfirstandroidtvapp.presentation.sections.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.myfirstandroidtvapp.R
import com.example.myfirstandroidtvapp.presentation.NavItem
import com.example.myfirstandroidtvapp.presentation.sections.dashboard.DashBoardScreen
import com.example.myfirstandroidtvapp.presentation.sections.home.sidebar.Sidebar
import com.example.myfirstandroidtvapp.presentation.sections.movies.MovieScreen
import com.example.myfirstandroidtvapp.presentation.sections.search.SearchScreen
import com.example.myfirstandroidtvapp.presentation.sections.series.SeriesScreen
import com.example.myfirstandroidtvapp.presentation.sections.settings.SettingsScreen

object FocusUtils {
    private var lastItemFocused: NavItem = NavItem.Dashboard

    fun getLastFocusedItem(): NavItem {
        return lastItemFocused
    }

    fun setLastFocusedItem(item: NavItem) {
        lastItemFocused = item
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    var lastSelectedItem by remember { mutableStateOf(NavItem.Dashboard) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.home_background),
            contentDescription = "Background Image",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )
        Row(modifier = Modifier.fillMaxSize()) {
            Sidebar(
                navController = navController,
                lastSelectedItem = lastSelectedItem,
                onItemSelected = { lastSelectedItem = it }
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            ) {
                when (lastSelectedItem) {
                    NavItem.Search -> SearchScreen()
                    NavItem.Dashboard -> DashBoardScreen(navController)
                    NavItem.Movies -> MovieScreen()
                    NavItem.Series -> SeriesScreen()
                    NavItem.Settings -> SettingsScreen()
                }
            }
        }
    }
}