package com.example.myfirstandroidtvapp.data.remote.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
data class GeneralResponse(
    @SerializedName("auto_play") val autoPlay: Boolean = false,
    @SerializedName("login_required") val loginRequired: Boolean = false,
    @SerializedName("auto_link_enabled") val autoLinkEnabled: Boolean = false,
    @SerializedName("allow_user_register") val allowUserRegister: Boolean = false,
    @SerializedName("allow_in_app_purchase") val allowInAppPurchase: Boolean = false
)

data class NotificationResponse(
    @SerializedName("notice") val notice: String = "",
    @SerializedName("notice_bar_color") val noticeBarColor: String = "",
    @SerializedName("notice_text_color") val noticeTextColor: String = ""
)

data class FaqItemResponse(
    @SerializedName("answer") val answer: String = "",
    @SerializedName("question") val question: String = ""
)

data class ContactInfoResponse(
    @SerializedName("name") val name: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("phone") val phone: String = "",
    @SerializedName("website") val website: String = ""
)

data class ExternalUrlsResponse(
    @SerializedName("about_us") val aboutUs: String = "",
    @SerializedName("signup_url") val signupUrl: String = "",
    @SerializedName("ads_tag_url") val adsTagUrl: String = "",
    @SerializedName("privacy_policy_url") val privacyPolicyUrl: String = "",
    @SerializedName("custom_purchase_url") val customPurchaseURL: String = ""
)

data class PackageResponse(
    @SerializedName("uuid") val uuid: String = "",
    @SerializedName("general") val general: GeneralResponse = GeneralResponse(),
    @SerializedName("notification") val notification: NotificationResponse = NotificationResponse(),
    @SerializedName("contact_info") val contactInfo: ContactInfoResponse = ContactInfoResponse(),
    @SerializedName("external_urls") val externalUrls: ExternalUrlsResponse = ExternalUrlsResponse(),
    @SerializedName("faq") val faq: List<FaqItemResponse> = emptyList(),
    @SerializedName("tenant_uuid") val tenantUuid: String = "",
    @SerializedName("access_key") val accessKey: String = "",
    @SerializedName("template_id") val templateId: String = "",
    @SerializedName("public_content_urls_enabled") val publicContentUrlsEnabled: Boolean = false
)

data class PublishedSite(
    @SerializedName("uuid") val uuid: String = "",
    @SerializedName("domain") val domain: String = "",
    @SerializedName("is_published") val isPublished: Boolean = false,
    @SerializedName("category") val category: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("base_url") val baseURL: String = "",
    @SerializedName("supportEmail") val supportEmail: String = ""
)

data class AdvancedConfig(
    @SerializedName("disable_subscription_fire_tv") val disableSubscriptionFireTV: Boolean = false,
    @SerializedName("disable_subscription_android_tv") val disableSubscriptionAndroidTV: Boolean = false,
    @SerializedName("disable_qr_fire_tv") val disableQRInFireTV: Boolean = false,
    @SerializedName("disable_qr_android_tv") val disableQRInAndroidTV: Boolean = false,
    @SerializedName("payment_website_type") val paymentWebsiteType: Int = 1,
    @SerializedName("android_tv_app_store_url") val androidTVAppStoreUrl: String = "",
    @SerializedName("fire_tv_app_store_url") val fireTVAppStoreUrl: String = ""
)

data class ConfigResponse(
    @SerializedName("package") val packageInfo: PackageResponse = PackageResponse(),
    @SerializedName("published_sites") val publishedSites: List<PublishedSite> = emptyList(),
    @SerializedName("json_app_config") val advancedConfig: AdvancedConfig = AdvancedConfig()
)