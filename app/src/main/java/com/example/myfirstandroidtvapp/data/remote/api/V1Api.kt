package com.example.myfirstandroidtvapp.data.remote.api

import com.example.myfirstandroidtvapp.data.remote.dto.ChannelListResponse
import com.example.myfirstandroidtvapp.data.remote.dto.ConfigResponse
import com.example.myfirstandroidtvapp.data.remote.dto.VodCategoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface V1Api {
    @GET("clients/vod-category/")
    suspend fun getVodCategories(
        @Query("has_video") hasVideo: Boolean = true,
        @Header("Authorization") authHeader: String
    ): Response<VodCategoryResponse>

    @GET("app/configs")
    suspend fun getServerConfig(
        @Query("tenant_id") tenantId: String
    ): ConfigResponse

    @GET("clients/channel/")
    suspend fun getChannels(
        @Query("page_size") pageSize: Int = 500,
        @Header("Authorization") authHeader: String
    ): ChannelListResponse
}