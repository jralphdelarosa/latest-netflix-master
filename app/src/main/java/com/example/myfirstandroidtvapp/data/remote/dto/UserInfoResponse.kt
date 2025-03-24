package com.example.myfirstandroidtvapp.data.remote.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
data class UserInfoResponse(
    @Expose
    @SerializedName("id") val id: Int = 0,

    @Expose
    @SerializedName("username") val username: String = "",

    @Expose
    @SerializedName("email") val email: String = ""
)