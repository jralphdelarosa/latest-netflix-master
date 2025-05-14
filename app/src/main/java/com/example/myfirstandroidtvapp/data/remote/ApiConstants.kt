package com.example.myfirstandroidtvapp.data.remote

/**
 * Created by John Ralph Dela Rosa on 3/23/2025.
 */
object ApiConstants {
    const val API_BASE: String = "https://api.tvstartupcms.com/api/v1/"
    const val API_V3_BASE: String = "https://api.tvstartupcms.com/api/v3/"

    const val BASE_CMS_URL = "https://tvstartupcms.com/"
    const val TENANT_ID: String = "e19a54df-1580-4b83-afc3-e062ef77af40"

//    Powertube
//    41ea2c28-418e-4424-aa4f-37ea150b662b
//
//
//    DiamondFlix
//    e19a54df-1580-4b83-afc3-e062ef77af40
//
//    Guyana
//    b5d28e52-3954-4c1e-ba44-143f434ee19b
//    Got one
//    462a87f9-7fcb-4088-8fb0-64a1c3ace6a2

    var PAYMENT_WEBSITE_TYPE = 1

    object Auth {
        const val LOGIN = "clients/auth/$TENANT_ID/login/"
        const val AUTO_LOGIN = "clients/auth/$TENANT_ID/auto-login/"
        const val REFRESH = "auth/refresh/"
    }

    object Config {
        const val APP_CONFIG = "$API_BASE/app/configs?tenant_id=$TENANT_ID"
    }

    object Video {
        const val CATEGORY = "$API_BASE/video-categories/?page=1&page_size=100&ordering=sort_order"
        fun videoList(categoryId: String) = "$API_BASE/clients/vod-category/$categoryId/videos?page=1&page_size=100"
        fun videoDetail(categoryId: String) = "$API_BASE/clients/vod-category/$categoryId?has_video=true"
    }

    object Audio {
        const val CATEGORY = "$API_BASE/clients/aod-category/?page=1&page_size=100"
        fun audioList(categoryId: String) = "$API_BASE/clients/aod-category/$categoryId/audios?page=1&page_size=100"
        fun audioDetail(categoryId: String) = "$API_BASE/clients/aod-category/$categoryId?has_audio=true"
    }

    object Channel {
        const val LIST = "$API_BASE/clients/channel/"
    }

    object Search {
        fun searchVideo(keyword: String) = "$API_BASE/clients/video/search?keyword=$keyword"
    }

    object Subscription {
        const val APP_CONTENT = "$API_BASE/billing/subscription/app-content/"
        const val LIST = "$API_BASE/billing/subscriber/retrieve/"
        const val REMOVE = "$API_BASE/billing/subscription/unsubscribe/"
    }

    object Favorite {
        const val LIST_CHANNEL = "${API_V3_BASE}favorite/list/?content_type=channel&user_owned=True"
        const val REMOVE = "${API_V3_BASE}favorite/delete/"
        const val ADD = "${API_V3_BASE}favorite/save/"
    }

    object Bookmark {
        const val SAVE_POSITION = "${API_V3_BASE}playback-position/save/"
    }

    object Billing {
        fun generateQrCode(data: String) = "$API_BASE/billing/order/qr-code?data=$data"
    }

    object Playback {
        fun getVideoPackage(id: String) = "$API_BASE/media/single/4/$id"
        fun getAudioPackage(id: String) = "$API_BASE/media/single/5/$id"
    }

    object Events {
        const val LIST = "$API_BASE/clients/event-ticket/list/"
    }

    object Stats {
        const val STAT = "$API_BASE/clients/stat/"
    }

    object Series {
        const val AUDIO_SERIES = "$API_BASE/clients/aod-category/?type=series"
        const val VIDEO_SERIES = "$API_BASE/clients/vod-category/?type=series"
    }
}