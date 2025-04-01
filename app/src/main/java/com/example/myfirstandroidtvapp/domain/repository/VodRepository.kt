package com.example.myfirstandroidtvapp.domain.repository

import com.example.myfirstandroidtvapp.data.remote.dto.VodCategoryResponse

interface VodRepository {
    suspend fun fetchVodCategories(): Result<VodCategoryResponse>
}