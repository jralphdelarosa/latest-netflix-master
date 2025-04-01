package com.example.myfirstandroidtvapp.data.remote.api

import com.example.myfirstandroidtvapp.data.remote.dto.VodCategoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface VodApi {
    @GET("clients/vod-category/")
    suspend fun getVodCategories(
        @Query("has_video") hasVideo: Boolean = true,
        @Header("Authorization") authHeader: String
    ): Response<VodCategoryResponse>
}