package com.example.myfirstandroidtvapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
@Parcelize
data class PPVEventUiModel(
    val uuid: String?,
    val priceCurrency: String?,
    val price: String?,
    val name: String?,
    val description: String?,
    val eventDates: String?,
    val preRegistrationDates: String?,
    val channelId: String?,
    val templateId: String?,
    val imageUrl: String?,
    val qrCodeUrl: String?,
    val tenantId: String?,
    val isPurchased: Boolean
) : Parcelable