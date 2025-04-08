package com.example.myfirstandroidtvapp.data.remote.dto

import android.content.ClipDescription
import com.google.gson.annotations.SerializedName

data class VodCategory(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String, // This is the category title
    @SerializedName("short_description") val shortDescription: String = "",
    @SerializedName("long_description") val longDescription: String = "",
    @SerializedName("videos") val videos: List<VodVideo>?,
    @SerializedName("thumbnail") val thumbnail: String = "",
    @SerializedName("children") val children: List<VodCategory> = emptyList(),
    @SerializedName("type") val type: String = ""
)