package com.example.myfirstandroidtvapp.data.remote.request

import com.google.gson.annotations.SerializedName

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
data class AutoLoginRequest(
    @SerializedName("deviceType") val deviceType: String,
    @SerializedName("deviceId") val deviceId: String
)