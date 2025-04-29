package com.example.myfirstandroidtvapp.data.remote.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubscriptionPlan(
    @SerializedName("uuid") val uuid: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("channel") val channel: String = "",
    @SerializedName("content_uuid") val contentUuid: String? = null,
    @SerializedName("content_type") val contentType: String? = null,
    @SerializedName("price_currency") val priceCurrency: String = "USD",
    @SerializedName("price") val price: String? = null,
    @SerializedName("is_active") val isActive: Boolean? = null,
    @SerializedName("is_free") val isFree: Boolean = false,
    @SerializedName("purchase_link_type") val purchaseLinkType: Int? = null,
    @SerializedName("template") val template: String? = null,
    @SerializedName("expires_in") val expiresIn: String = "",
    @SerializedName("trial_in") val trialIn: String = "",
    @SerializedName("discount_currency") val discountCurrency: String = "USD",
    @SerializedName("discount") val discount: String = "0.00",
    @SerializedName("membership_color") val membershipColor: String = "",
    @SerializedName("act_btn_color") val actBtnColor: String = ""
) : Parcelable {

    val formattedExpiresIn: String
        get() = when (expiresIn) {
            "1 months" -> "Month"
            "1 weeks" -> "Week"
            "1 days" -> "Day"
            "1 years" -> "Year"
            "undefined undefined" -> ""
            else -> expiresIn
        }

    val priceWithSign: String
        get() = "${toCurrencySign(priceCurrency)} $price"

    val discountWithSign: String
        get() = "${toCurrencySign(discountCurrency)} $discount"

    private fun toCurrencySign(currency: String): String = when (currency) {
        "USD" -> "$"
        "EUR" -> "€"
        "GBP" -> "£"
        "BRL" -> "R$"
        "NIO" -> "C$"
        "GTQ" -> "Q"
        "HNL" -> "L"
        else -> "$"
    }

    override fun toString(): String = """
        uuid = $uuid
        title = $title
        price = $price
        expiresIn = $expiresIn
        discount = $discount
        trialIn = $trialIn
        isFree = $isFree
    """.trimIndent()
}