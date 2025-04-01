package com.example.myfirstandroidtvapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VodCategory(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String, // This is the category title
    @SerializedName("videos") val videos: List<VodVideo>?
)