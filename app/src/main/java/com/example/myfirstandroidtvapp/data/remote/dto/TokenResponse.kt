package com.example.myfirstandroidtvapp.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
data class TokenResponse(
    @SerializedName("access") val access: String = "",
    @SerializedName("refresh") val refresh: String = ""
)