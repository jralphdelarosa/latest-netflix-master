package com.example.myfirstandroidtvapp.data.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
data class RefreshTokenRequest(
    @Expose
    @SerializedName("refresh") val refresh: String
)