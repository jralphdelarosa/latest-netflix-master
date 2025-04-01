package com.example.myfirstandroidtvapp.data.repository

import com.example.myfirstandroidtvapp.data.local.usersharedpref.UserSharedPref
import com.example.myfirstandroidtvapp.data.remote.api.ApiResponse
import com.example.myfirstandroidtvapp.data.remote.api.VodApi
import com.example.myfirstandroidtvapp.data.remote.dto.VodCategoryResponse
import com.example.myfirstandroidtvapp.domain.repository.UserRepository
import com.example.myfirstandroidtvapp.domain.repository.VodRepository
import timber.log.Timber
import javax.inject.Inject

class VodRepositoryImpl @Inject constructor(
    private val apiService: VodApi,
    private val userPref: UserSharedPref,
    private val userRepository: UserRepository // Ensure this has refreshToken()
) : VodRepository {

    override suspend fun fetchVodCategories(): Result<VodCategoryResponse> {

        return try {

            var token = userPref.getAccessToken() // Get current token

            // If token is missing, try refreshing it
            if (token.isNullOrBlank()) {
                val refreshResponse = userRepository.refreshToken()
                if (refreshResponse is ApiResponse.Success) {
                    token = refreshResponse.data.token?.access // Get new token
                } else {
                    return Result.failure(Exception("Failed to refresh token"))
                }
            }


            val response = apiService.getVodCategories(authHeader = "Bearer $token")

            Timber.tag("vod response").d("Response body: ${response.body()?.toString()}")

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Timber.tag("vod response").d("${e.message}")
            Result.failure(e)
        }
    }
}