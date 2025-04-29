package com.example.myfirstandroidtvapp.data.repository

import com.example.myfirstandroidtvapp.TvCoreApplication
import com.example.myfirstandroidtvapp.data.local.usersharedpref.UserSharedPref
import com.example.myfirstandroidtvapp.data.remote.api.ApiResponse
import com.example.myfirstandroidtvapp.data.remote.api.V1Api
import com.example.myfirstandroidtvapp.data.remote.dto.ChannelListResponse
import com.example.myfirstandroidtvapp.data.remote.dto.VodCategoryResponse
import com.example.myfirstandroidtvapp.domain.repository.UserRepository
import com.example.myfirstandroidtvapp.domain.repository.ContentsRepository
import timber.log.Timber
import javax.inject.Inject

class ContentsRepositoryImpl @Inject constructor(
    private val apiService: V1Api,
    private val userPref: UserSharedPref,
    private val userRepository: UserRepository // Ensure this has refreshToken()
) : ContentsRepository {

    companion object {
        private const val CACHE_KEY_CHANNELS = "cache_channels"
        private const val CACHE_DURATION = 60 * 60 * 1000L // 1 hour in milliseconds
    }

    override suspend fun getChannels(): ChannelListResponse {
        var accessKey = userPref.getAccessToken() // Get current token

        // If token is missing, try refreshing it
        if (accessKey.isNullOrBlank()) {
            val refreshResponse = userRepository.refreshToken()
            if (refreshResponse is ApiResponse.Success) {
                accessKey = refreshResponse.data.token?.access // Get new token
            }
        }

        Timber.tag("vod response").d("Access token: $accessKey")

        return apiService.getChannels(authHeader = buildAuthHeader(accessKey.toString()))
    }

    override suspend fun fetchVodCategories(): Result<VodCategoryResponse> {

        return try {

            var accessKey = userPref.getAccessToken() // Get current token

            // If token is missing, try refreshing it
            if (accessKey.isNullOrBlank()) {
                val refreshResponse = userRepository.refreshToken()
                if (refreshResponse is ApiResponse.Success) {
                    accessKey = refreshResponse.data.token?.access // Get new token
                } else {
                    return Result.failure(Exception("Failed to refresh token"))
                }
            }

            Timber.tag("vod response").d("Access token: $accessKey")

            val response = apiService.getVodCategories(authHeader = buildAuthHeader(accessKey.toString()))

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

    fun buildAuthHeader(accessKey:String): String {
        return if (TvCoreApplication.isUserLoggedIn.value == true) "Bearer $accessKey" else "Tenant-Key $accessKey"
    }
}