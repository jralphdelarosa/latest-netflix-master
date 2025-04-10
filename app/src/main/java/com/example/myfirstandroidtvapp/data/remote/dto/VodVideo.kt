package com.example.myfirstandroidtvapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VodVideo(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("thumbnail") val thumbnail: String = "",
    @SerializedName("trailer") val trailer: TrailerResponse?,
    @SerializedName("title_logo") val titleLogo: String = "",
    @SerializedName("year_of_release") val yearRelease: String = "",
    @SerializedName("duration") val duration: String = "",
    @SerializedName("url") val url: String? = null
)