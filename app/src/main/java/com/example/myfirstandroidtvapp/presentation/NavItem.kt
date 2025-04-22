package com.example.myfirstandroidtvapp.presentation

enum class NavItem(val title: String, val route: String) {
    Search("Search", "search"),
    Dashboard("Dashboard", "dashboard"),
    Movies("Movies", "movies"),
    Series("Series", "series"),
    TvGuide("TvGuide", "tvguide"),
    Settings("Settings", "settings")
}