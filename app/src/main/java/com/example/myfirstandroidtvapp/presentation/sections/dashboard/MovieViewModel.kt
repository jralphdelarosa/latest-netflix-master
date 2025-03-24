package com.example.myfirstandroidtvapp.presentation.sections.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirstandroidtvapp.domain.model.Movie
import com.example.myfirstandroidtvapp.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
//class MovieViewModel @Inject constructor(
//    private val repository: MovieRepository
//) : ViewModel() {
//
//    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
//    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()
//
//    private val _movieTrailer = MutableStateFlow<String?>(null)
//    val movieTrailer: StateFlow<String?> = _movieTrailer.asStateFlow()
//
//    private val _movieDetail = MutableStateFlow<Movie?>(null)
//    val movieDetail: StateFlow<Movie?> = _movieDetail.asStateFlow()
//
//    fun fetchPopularMovies(apiKey: String) {
//        viewModelScope.launch {
//            _movies.value = repository.getPopularMovies(apiKey)
//        }
//    }
//
//    fun fetchMovieDetails(movieId: Int, apiKey: String) {
//        viewModelScope.launch {
//            _movieDetail.value = repository.getMovieDetails(movieId, apiKey)
//        }
//    }
//
//    fun fetchMovieTrailer(movieId: Int, apiKey: String) {
//        viewModelScope.launch {
//            try {
//                val response = repository.getMovieVideos(movieId, apiKey)
//                val trailer = response.results.firstOrNull { it.type == "Trailer" && it.site == "YouTube" }
//                _movieTrailer.value = trailer?.key // Store YouTube video key
//            } catch (e: Exception) {
//                Log.e("MovieViewModel", "Error fetching trailer: ${e.message}")
//            }
//        }
//    }
//}