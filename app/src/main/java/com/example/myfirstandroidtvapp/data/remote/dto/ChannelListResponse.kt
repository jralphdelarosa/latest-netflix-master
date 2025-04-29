package com.example.myfirstandroidtvapp.data.remote.dto

import android.telephony.SubscriptionPlan
import com.google.gson.annotations.SerializedName

data class ChannelListResponse(
    @SerializedName("count") val count: Int? = null,
    @SerializedName("next") val next: String? = null,
    @SerializedName("previous") val previous: String? = null,
    @SerializedName("results") val results: List<ChannelResponse> = emptyList()
)

data class PlaylistVideoResponse(
    @SerializedName("uuid") val uuid: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("source_file") val sourceFile: String? = null,
    @SerializedName("output_file") val outputFile: String? = null,
    @SerializedName("thumbnail") val thumbnail: String? = null,
    @SerializedName("duration") val duration: String? = null,
    @SerializedName("description") val description: String? = null
)

data class PlaylistVideoOrderResponse(
    @SerializedName("uuid") val uuid: String? = null,
    @SerializedName("video") val video: PlaylistVideoResponse? = null
)

data class PlaylistResponse(
    @SerializedName("uuid") val uuid: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("start_date") val startDate: String? = null,
    @SerializedName("end_date") val endDate: String? = null,
    @SerializedName("duration") val duration: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("videos_order") val videosOrder: List<PlaylistVideoOrderResponse>? = null
)

data class ChannelResponse(
    @SerializedName("id") val id: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("short_description") val shortDescription: String? = null,
    @SerializedName("long_description") val longDescription: String? = null,
    @SerializedName("thumbnail") val thumbnail: String? = null,
    @SerializedName("stream_mode") val streamMode: String? = null,
    @SerializedName("stream_url") val streamUrl: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("current_playing_time") val currentPlayingTime: Int? = null,
    @SerializedName("playlists") val playlists: List<PlaylistResponse>? = null,
    @SerializedName("live_stream_subscription_plans") val subscriptionPlans: List<SubscriptionPlan> = emptyList(),
    @SerializedName("credentials") val credentials: CredentialResponse? = null
)