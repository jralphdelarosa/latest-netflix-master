package com.example.myfirstandroidtvapp.domain.repository

import com.example.myfirstandroidtvapp.data.remote.dto.VideoResponse
import com.example.myfirstandroidtvapp.domain.model.Movie

interface MovieRepository {
    suspend fun getPopularMovies(apiKey: String): List<Movie>
    suspend fun getMovieDetails(movieId: Int, apiKey: String): Movie
    suspend fun getMovieVideos(movieId: Int, apiKey: String): VideoResponse
}