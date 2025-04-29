package com.example.myfirstandroidtvapp.domain.repository

import com.example.myfirstandroidtvapp.data.remote.dto.ChannelListResponse
import com.example.myfirstandroidtvapp.data.remote.dto.VodCategoryResponse

interface ContentsRepository {
    suspend fun fetchVodCategories(): Result<VodCategoryResponse>
    suspend fun getChannels(): ChannelListResponse
}