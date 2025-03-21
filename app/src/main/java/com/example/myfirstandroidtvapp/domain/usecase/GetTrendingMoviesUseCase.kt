package com.example.myfirstandroidtvapp.domain.usecase

import com.example.myfirstandroidtvapp.domain.model.Movie
import com.example.myfirstandroidtvapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetTrendingMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    //suspend operator fun invoke(): List<Movie> = repository.getPopularMovies()
}