package com.example.myfirstandroidtvapp.data.local.usersharedpref

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
@Singleton
class UserSharedPrefImpl @Inject constructor(
    context: Context,
    prefFileName: String
) : UserSharedPref {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)

    override fun getAccessToken(): String? =
        sharedPreferences.getString(PREF_KEY_ACCESS_TOKEN, null)

    override fun setAccessToken(accessToken: String?) {
        accessToken?.takeIf { it != "Bearer null" }?.let {
            sharedPreferences.edit().putString(PREF_KEY_ACCESS_TOKEN, it).apply()
        } ?: run {
            sharedPreferences.edit().remove(PREF_KEY_ACCESS_TOKEN).apply()
        }
    }

    override fun getRefreshToken(): String? =
        sharedPreferences.getString(PREF_KEY_REFRESH_TOKEN, null)

    override fun setRefreshToken(refreshToken: String?) {
        refreshToken?.let {
            sharedPreferences.edit().putString(PREF_KEY_REFRESH_TOKEN, it).apply()
        } ?: sharedPreferences.edit().remove(PREF_KEY_REFRESH_TOKEN).apply()
    }

    override fun getAutoPlayConfig(): Boolean? {
        TODO("Not yet implemented")
    }

    override fun setAutoPlay(autoPlay: Boolean?) {
        sharedPreferences.edit()
            .putBoolean("AUTO_PLAY", true)
            .apply()
    }

    override fun getDeviceId(): String {
        TODO("Not yet implemented")
    }

    override fun getUserAccessToken(): String? =
        sharedPreferences.getString(PREF_KEY_USER_ACCESS_TOKEN, null)

    override fun setUserAccessToken(accessToken: String?) {
        accessToken?.takeIf { it != "Bearer null" }?.let {
            sharedPreferences.edit().putString(PREF_KEY_USER_ACCESS_TOKEN, it).apply()
        } ?: run {
            sharedPreferences.edit().remove(PREF_KEY_USER_ACCESS_TOKEN).apply()
        }
    }

    override fun getUserRefreshToken(): String? =
        sharedPreferences.getString(PREF_KEY_USER_REFRESH_TOKEN, null)

    override fun setUserRefreshToken(refreshToken: String?) {
        refreshToken?.let {
            sharedPreferences.edit().putString(PREF_KEY_USER_REFRESH_TOKEN, it).apply()
        } ?: sharedPreferences.edit().remove(PREF_KEY_USER_REFRESH_TOKEN).apply()
    }

    override fun setTemplateId(templateId: String?) {
        TODO("Not yet implemented")
    }

    override fun getTemplateId(): String {
        TODO("Not yet implemented")
    }

    override fun isUserLoggedIn(): Boolean =
        sharedPreferences.getBoolean(USER_LOGGED_IN, false)

    override fun getBookmark(objectId: String): String? {
        TODO("Not yet implemented")
    }

    override fun setBookmark(objectId: String, position: String) {
        TODO("Not yet implemented")
    }

    override fun setUserLoggedIn(loggedIn: Boolean?) {
        loggedIn?.let {
            sharedPreferences.edit().putBoolean(USER_LOGGED_IN, it).apply()
        } ?: sharedPreferences.edit().remove(USER_LOGGED_IN).apply()
    }

    override fun getDeviceUUID(): String? =
        sharedPreferences.getString(DEVICE_UUID, null)

    override fun setDeviceUUID(uuid: String?) {
        uuid?.let {
            sharedPreferences.edit().putString(DEVICE_UUID, it).apply()
        } ?: sharedPreferences.edit().remove(DEVICE_UUID).apply()
    }

    companion object {
        private const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
        private const val PREF_KEY_REFRESH_TOKEN = "PREF_KEY_REFRESH_TOKEN"
        private const val PREF_KEY_USER_ACCESS_TOKEN = "PREF_KEY_USER_ACCESS_TOKEN"
        private const val PREF_KEY_USER_REFRESH_TOKEN = "PREF_KEY_USER_REFRESH_TOKEN"
        private const val USER_LOGGED_IN = "USER_LOGGED_IN"
        private const val DEVICE_UUID = "DEVICE_UUID"
    }
}