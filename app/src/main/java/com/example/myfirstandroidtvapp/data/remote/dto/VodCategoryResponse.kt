package com.example.myfirstandroidtvapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VodCategoryResponse(
    @SerializedName("results") val results: List<VodCategory>
)