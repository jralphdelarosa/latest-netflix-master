package com.example.myfirstandroidtvapp.data.remote

import com.example.myfirstandroidtvapp.data.remote.dto.MovieResponse
import com.example.myfirstandroidtvapp.data.remote.dto.VideoResponse
import com.example.myfirstandroidtvapp.domain.model.Movie
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @GET(TmdbApiConstants.POPULAR_MOVIES)
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = TmdbApiConstants.API_KEY
    ): MovieResponse

    @GET(TmdbApiConstants.MOVIE_DETAILS)
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = TmdbApiConstants.API_KEY
    ): Movie

    @GET(TmdbApiConstants.MOVIE_VIDEOS)
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = TmdbApiConstants.API_KEY
    ): VideoResponse


}