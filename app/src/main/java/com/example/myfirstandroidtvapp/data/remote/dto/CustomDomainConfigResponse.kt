package com.example.myfirstandroidtvapp.data.remote.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
data class CustomDomainConfigResponse(
    @Expose @SerializedName("video_player_url") val videoPlayerURL: String = "",
    @Expose @SerializedName("audio_player_url") val audioPlayerURL: String = "",
    @Expose @SerializedName("livestream_player_url") val livestreamPlayerURL: String = "",
    @Expose @SerializedName("subscription_url") val subscriptionURL: String = "",
    @Expose @SerializedName("ppv_url") val ppvURL: String = "",
    @Expose @SerializedName("login_url") val loginURL: String = "",
    @Expose @SerializedName("free_register") val freeRegisterURL: String = ""
)