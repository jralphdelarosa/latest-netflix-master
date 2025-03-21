package com.example.myfirstandroidtvapp.data.repository

import android.util.Log
import com.example.myfirstandroidtvapp.data.remote.MovieApiService
import com.example.myfirstandroidtvapp.data.remote.dto.VideoResponse
import com.example.myfirstandroidtvapp.domain.model.Movie
import com.example.myfirstandroidtvapp.domain.repository.MovieRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService
) : MovieRepository {

    override suspend fun getPopularMovies(apiKey: String): List<Movie> {
        return try {
            apiService.getPopularMovies(apiKey).results
        } catch (e: Exception) {
            Log.e("MovieRepositoryImpl", "Error fetching popular movies", e)
            emptyList()
        }
    }

    override suspend fun getMovieDetails(movieId: Int, apiKey: String): Movie {
        return try {
            apiService.getMovieDetails(movieId, apiKey)
        } catch (e: Exception) {
            Log.e("MovieRepositoryImpl", "Error fetching movie details", e)
            throw e
        }
    }

    override suspend fun getMovieVideos(movieId: Int, apiKey: String): VideoResponse {
        return apiService.getMovieVideos(movieId, apiKey)
    }
}