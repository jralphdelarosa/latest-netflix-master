package com.example.myfirstandroidtvapp.data.remote.dto
import com.example.myfirstandroidtvapp.domain.model.Video

data class VideoResponse(
    val id: Int,
    val results: List<Video>
)