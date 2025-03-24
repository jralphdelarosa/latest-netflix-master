package com.example.myfirstandroidtvapp.data.remote.request

import com.google.gson.annotations.SerializedName

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
data class LoginRequest(
    @SerializedName("username") val username: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("password") val password: String
)