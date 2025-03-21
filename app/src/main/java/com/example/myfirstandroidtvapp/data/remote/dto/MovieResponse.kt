package com.example.myfirstandroidtvapp.data.remote.dto

import com.example.myfirstandroidtvapp.domain.model.Movie
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("results") val results: List<Movie>
)