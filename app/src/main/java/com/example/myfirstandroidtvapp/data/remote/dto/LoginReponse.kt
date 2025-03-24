package com.example.myfirstandroidtvapp.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
data class LoginResponse(
    @SerializedName("token") val token: TokenResponse? = null,
    @SerializedName("profile") val profile: UserInfoResponse? = null,
    @SerializedName("access_token") val accessToken: String = "",
    @SerializedName("refresh_token") val refreshToken: String = ""
)