package com.example.myfirstandroidtvapp.data.local.usersharedpref

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
interface UserSharedPref {
    fun getAccessToken(): String?

    fun setAccessToken(accessToken: String?)

    fun getRefreshToken(): String?

    fun setRefreshToken(refreshToken: String?)

    fun getAutoPlayConfig(): Boolean?

    fun setAutoPlay(autoPlay: Boolean?)

    fun getDeviceId(): String
    fun getUserAccessToken(): String?

    fun setUserAccessToken(accessToken: String?)

    fun getUserRefreshToken(): String?

    fun setUserRefreshToken(refreshToken: String?)
    fun setTemplateId(templateId: String?)
    fun getTemplateId(): String
    fun setDeviceUUID(uuid: String?)
    fun getDeviceUUID(): String?
    fun setUserLoggedIn(loggedIN: Boolean?)
    fun isUserLoggedIn(): Boolean?
    fun getBookmark(objectId: String): String?
    fun setBookmark(objectId: String, position: String)
}