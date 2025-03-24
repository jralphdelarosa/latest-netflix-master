package com.example.myfirstandroidtvapp.data.remote.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
data class QrCodeResponse(
    @Expose @SerializedName("error") val error: String? = null,
    @Expose @SerializedName("value") val value: String? = null
)