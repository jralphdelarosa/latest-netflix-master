package com.example.myfirstandroidtvapp.data.remote.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.example.myfirstandroidtvapp.domain.model.PPVEventUiModel
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
@Parcelize
data class PPVEventResponse(
    @Expose @SerializedName("credentials") val credentials: CredentialResponse? = null,
    @Expose @SerializedName("event_list") var eventList: List<EventListResponse> = emptyList()
) : Parcelable

@Parcelize
data class CredentialResponse(
    @Expose @SerializedName("token") val token: Token? = null,
    @Expose @SerializedName("profile") val profile: Profile? = null,
    @Expose @SerializedName("message") var message: String? = null
) : Parcelable

@Parcelize
data class Token(
    @Expose @SerializedName("refresh") val refresh: String? = null,
    @Expose @SerializedName("access") val access: String? = null
) : Parcelable

@Parcelize
data class Profile(
    @Expose @SerializedName("id") val id: String? = null,
    @Expose @SerializedName("username") val username: String? = null,
    @Expose @SerializedName("email") val email: String? = null
) : Parcelable

@Parcelize
data class EventListResponse(
    @Expose @SerializedName("uuid") val uuid: String? = null,
    @Expose @SerializedName("price_currency") val priceCurrency: String? = null,
    @Expose @SerializedName("price") val price: String? = null,
    @Expose @SerializedName("name") val name: String? = null,
    @Expose @SerializedName("description") val description: String? = null,
    @Expose @SerializedName("event_dates") val eventDates: String = "",
    @Expose @SerializedName("pre_registration_dates") val preRegistrationDates: String? = null,
    @Expose @SerializedName("channel_id") val channelId: String? = null,
    @Expose @SerializedName("template_id") val templateId: String? = null,
    @Expose @SerializedName("image_url") val imageUrl: String? = null,
    @Expose @SerializedName("qr_code_url") val qrCodeUrl: String? = null,
    @Expose @SerializedName("is_purchased") val isPurchased: Boolean = false,
    @Expose @SerializedName("tenant_id") val tenantId: String? = null
) : Parcelable

fun EventListResponse.toModel(): PPVEventUiModel {
    val constructor = PPVEventUiModel::class.primaryConstructor ?: throw IllegalArgumentException("No primary constructor found.")
    val propertiesByName = EventListResponse::class.memberProperties.associateBy { it.name }

    return constructor.callBy(constructor.parameters.associateWith { parameter ->
        propertiesByName[parameter.name]?.get(this)
    })
}