package com.example.myfirstandroidtvapp.data.remote.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
data class GeneralResponse(
    @Expose @SerializedName("auto_play") val autoPlay: Boolean = false,
    @Expose @SerializedName("login_required") val loginRequired: Boolean = false,
    @Expose @SerializedName("auto_link_enabled") val autoLinkEnabled: Boolean = false,
    @Expose @SerializedName("allow_user_register") val allowUserRegister: Boolean = false,
    @Expose @SerializedName("allow_in_app_purchase") val allowInAppPurchase: Boolean = false
)

data class NotificationResponse(
    @Expose @SerializedName("notice") val notice: String = "",
    @Expose @SerializedName("notice_bar_color") val noticeBarColor: String = "",
    @Expose @SerializedName("notice_text_color") val noticeTextColor: String = ""
)

data class FaqItemResponse(
    @Expose @SerializedName("answer") val answer: String = "",
    @Expose @SerializedName("question") val question: String = ""
)

data class ContactInfoResponse(
    @Expose @SerializedName("name") val name: String = "",
    @Expose @SerializedName("email") val email: String = "",
    @Expose @SerializedName("phone") val phone: String = "",
    @Expose @SerializedName("website") val website: String = ""
)

data class ExternalUrlsResponse(
    @Expose @SerializedName("about_us") val aboutUs: String = "",
    @Expose @SerializedName("signup_url") val signupUrl: String = "",
    @Expose @SerializedName("ads_tag_url") val adsTagUrl: String = "",
    @Expose @SerializedName("privacy_policy_url") val privacyPolicyUrl: String = "",
    @Expose @SerializedName("custom_purchase_url") val customPurchaseURL: String = ""
)

data class PackageResponse(
    @Expose @SerializedName("uuid") val uuid: String = "",
    @Expose @SerializedName("general") val general: GeneralResponse = GeneralResponse(),
    @Expose @SerializedName("notification") val notification: NotificationResponse = NotificationResponse(),
    @Expose @SerializedName("contact_info") val contactInfo: ContactInfoResponse = ContactInfoResponse(),
    @Expose @SerializedName("external_urls") val externalUrls: ExternalUrlsResponse = ExternalUrlsResponse(),
    @Expose @SerializedName("faq") val faq: List<FaqItemResponse> = emptyList(),
    @Expose @SerializedName("tenant_uuid") val tenantUuid: String = "",
    @Expose @SerializedName("access_key") val accessKey: String = "",
    @Expose @SerializedName("template_id") val templateId: String = "",
    @Expose @SerializedName("public_content_urls_enabled") val publicContentUrlsEnabled: Boolean = false
)

data class PublishedSite(
    @Expose @SerializedName("uuid") val uuid: String = "",
    @Expose @SerializedName("domain") val domain: String = "",
    @Expose @SerializedName("is_published") val isPublished: Boolean = false,
    @Expose @SerializedName("category") val category: Int = 0,
    @Expose @SerializedName("name") val name: String = "",
    @Expose @SerializedName("base_url") val baseURL: String = "",
    @Expose @SerializedName("supportEmail") val supportEmail: String = ""
)

data class AdvancedConfig(
    @Expose @SerializedName("disable_subscription_fire_tv") val disableSubscriptionFireTV: Boolean = false,
    @Expose @SerializedName("disable_subscription_android_tv") val disableSubscriptionAndroidTV: Boolean = false,
    @Expose @SerializedName("disable_qr_fire_tv") val disableQRInFireTV: Boolean = false,
    @Expose @SerializedName("disable_qr_android_tv") val disableQRInAndroidTV: Boolean = false,
    @Expose @SerializedName("payment_website_type") val paymentWebsiteType: Int = 1,
    @Expose @SerializedName("android_tv_app_store_url") val androidTVAppStoreUrl: String = "",
    @Expose @SerializedName("fire_tv_app_store_url") val fireTVAppStoreUrl: String = ""
)

data class ConfigResponse(
    @Expose @SerializedName("package") val packageInfo: PackageResponse = PackageResponse(),
    @Expose @SerializedName("published_sites") val publishedSites: List<PublishedSite> = emptyList(),
    @Expose @SerializedName("json_app_config") val advancedConfig: AdvancedConfig = AdvancedConfig()
)