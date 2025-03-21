package com.example.myfirstandroidtvapp.data.remote

object TmdbApiConstants {
    const val API_KEY = "a87c567f79e0c40056f90bf48f942d28"
    const val BASE_URL = "https://api.themoviedb.org/3/"

    // Movie Endpoints
    const val POPULAR_MOVIES = "movie/popular"
    const val MOVIE_DETAILS = "movie/{movie_id}"
    const val MOVIE_VIDEOS = "movie/{movie_id}/videos"

    // TV Show Endpoints
    const val POPULAR_TV_SHOWS = "tv/popular"
    const val TV_SHOW_DETAILS = "tv/{tv_id}"

    // Search Endpoints
    const val SEARCH_MOVIES = "search/movie"
    const val SEARCH_TV_SHOWS = "search/tv"
}