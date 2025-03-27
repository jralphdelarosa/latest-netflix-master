package com.example.myfirstandroidtvapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.example.myfirstandroidtvapp.TvCoreApplication
import com.example.myfirstandroidtvapp.data.local.usersharedpref.UserSharedPref
import com.example.myfirstandroidtvapp.data.remote.ApiEndPoint
import com.example.myfirstandroidtvapp.data.remote.api.ApiResponse
import com.example.myfirstandroidtvapp.data.remote.api.AuthApi
import com.example.myfirstandroidtvapp.data.remote.dto.ConfigResponse
import com.example.myfirstandroidtvapp.data.remote.dto.CredentialResponse
import com.example.myfirstandroidtvapp.data.remote.dto.CustomDomainConfigResponse
import com.example.myfirstandroidtvapp.data.remote.dto.LoginResponse
import com.example.myfirstandroidtvapp.data.remote.dto.QrCodeResponse
import com.example.myfirstandroidtvapp.data.remote.dto.RegisterResponse
import com.example.myfirstandroidtvapp.data.remote.request.AutoLoginRequest
import com.example.myfirstandroidtvapp.data.remote.request.LoginRequest
import com.example.myfirstandroidtvapp.data.remote.request.RefreshTokenRequest
import com.example.myfirstandroidtvapp.data.remote.request.RegisterRequest
import com.example.myfirstandroidtvapp.data.remote.util.ApiResult
import com.example.myfirstandroidtvapp.domain.repository.UserRepository
import com.google.gson.Gson
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val userPref: UserSharedPref,
    val context: Context
) : UserRepository {

    private var _currentConfig: ConfigResponse? = null
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "kotlinsharedpreference", Context.MODE_PRIVATE
    )
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun login(email: String, password: String): ApiResponse<LoginResponse> {
        return try {
            val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

            val request = if (email.matches(emailRegex)) {
                LoginRequest(username = "", email = email, password = password)  // Email login
            } else {
                LoginRequest(username = email, email = "", password = password)  // Username login
            }

            Timber.tag("LoginRequest").d(Gson().toJson(request))

            // Call API and wrap response
            val response = authApi.login(ApiEndPoint.TENANT_ID, request)
            ApiResponse.Success(response)

        } catch (e: HttpException) {
            Timber.tag("LoginAPI").e("HTTP Exception: ${e.message}")
            ApiResponse.Error(e.message ?: "HTTP error")
        } catch (e: IOException) {  // Handles network failures (no internet, timeout, etc.)
            ApiResponse.NetworkError

        } catch (e: Exception) {  // Handles any unexpected issues
            ApiResponse.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    override suspend fun register(request: RegisterRequest): ApiResult<RegisterResponse> {
        return try {
            val response = authApi.register(ApiEndPoint.TENANT_ID, request)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    ApiResult.Success(responseBody)
                } else {
                    ApiResult.Error("Empty response body")
                }
            } else {
                ApiResult.Error(response.errorBody()?.string() ?: "Unknown error")
            }
        } catch (e: Exception) {
            ApiResult.Error("An unexpected error occurred", e)
        }
    }

    override fun clearCurrentUserData() {
        // Clear stored tokens
        userPref.setUserAccessToken(null)
        userPref.setUserRefreshToken(null)
        userPref.setAccessToken(null)
        userPref.setRefreshToken(null)

        // Clear login status and preferences
        userPref.setUserLoggedIn(false)
        userPref.setAutoPlay(null)
        userPref.setDeviceUUID(null)

        // Clear SharedPreferences
        editor.remove("autoplay").apply()

        // Reset UUID or perform additional cleanup if needed
        TvCoreApplication.instance.setUUID()

        Timber.d("User session cleared successfully")
    }

    override suspend fun refreshToken(): ApiResponse<LoginResponse> {
        return try {
            val refreshToken = userPref.getUserRefreshToken()  // Fetch from SharedPreferences
            if (refreshToken.isNullOrEmpty()) {
                return ApiResponse.Error("No refresh token available")
            }

            val response = authApi.refreshToken(RefreshTokenRequest(refreshToken))
            if (response is ApiResponse.Success) {
                response.data.let { loginResponse ->
                    updateToken(loginResponse.accessToken, loginResponse.refreshToken) // Store new tokens
                }
            }
            response
        } catch (e: Exception) {
            ApiResponse.Error(e.localizedMessage ?: "Network error")
        }
    }
    override suspend fun getConfig(): ApiResponse<ConfigResponse> {
        return try {
            Timber.d("getConfig()")
            if (_currentConfig != null) {
                TvCoreApplication.isLoginRequired = _currentConfig!!.packageInfo?.general?.loginRequired!!
                return ApiResponse.Success(_currentConfig!!)
            }
            authApi.getServerConfig().also { response ->
                if (response is ApiResponse.Success) {
                    _currentConfig = response.data
                    TvCoreApplication.isLoginRequired = response.data.packageInfo?.general?.loginRequired!!
                    updateConfigValues(response.data)
                }
            }
        } catch (e: Exception) {
            Timber.e("failed to load config: ${e.localizedMessage}")
            ApiResponse.Error(e.localizedMessage ?: "Network error")
        }
    }

    override suspend fun loadCustomCMSConfig(): ApiResponse<CustomDomainConfigResponse> {
        return try {
            authApi.loadCustomDomainConfig(TvCoreApplication.customPurchaseURL)
        } catch (e: Exception) {
            ApiResponse.Error(e.localizedMessage ?: "Network error")
        }
    }

    override suspend fun generateQrCode(
        id: String, type: Int, purchaseLinkType: Int, categoryId: String?, templateId: String?
    ): ApiResponse<QrCodeResponse> {
        return try {
            val urlPath = generateQrCodePath(id, type, purchaseLinkType, categoryId, templateId)
            authApi.generateQrCode(urlPath)
        } catch (e: Exception) {
            ApiResponse.Error(e.localizedMessage ?: "Network error")
        }
    }

    override fun updateToken(accessToken: String?, refreshToken: String?) {
        accessToken?.let {
            userPref.setAccessToken("Tenant-Key $it")  // Store in SharedPreferences
        }
        refreshToken?.let {
            userPref.setRefreshToken(it)
        }
    }

    override fun updateUserToken(accessToken: String?, refreshToken: String?) {
        Timber.d("updateUserToken($accessToken, $refreshToken)")
        accessToken?.let {
            userPref.setUserAccessToken("Bearer $it")
            userPref.setUserLoggedIn(true)
            TvCoreApplication.isUserLoggedIn.value = true
        }
        refreshToken?.let {
            userPref.setUserRefreshToken(it)
        }
    }

    override suspend fun checkAutoLogin(): ApiResponse<CredentialResponse> {
        return try {
            val request = AutoLoginRequest(
                deviceType = TvCoreApplication.deviceType,
                deviceId = TvCoreApplication.deviceId
            )
            authApi.checkAutoLogin(request)
        } catch (e: Exception) {
            ApiResponse.Error(e.localizedMessage ?: "Network error")
        }
    }


    private fun updateConfigValues(config: ConfigResponse) {
        config.advancedConfig?.let {
            if (TvCoreApplication.deviceType == "ANDROIDTV") {
                TvCoreApplication.disableSubscription = it.disableSubscriptionAndroidTV
                TvCoreApplication.disableQR = it.disableQRInAndroidTV
                if (it.androidTVAppStoreUrl.isNotEmpty()) {
                    TvCoreApplication.appStoreUrl = it.androidTVAppStoreUrl
                }
            } else if (TvCoreApplication.deviceType == "FIRETV") {
                TvCoreApplication.disableSubscription = it.disableSubscriptionFireTV
                TvCoreApplication.disableQR = it.disableQRInFireTV
                if (it.fireTVAppStoreUrl.isNotEmpty()) {
                    TvCoreApplication.appStoreUrl = it.fireTVAppStoreUrl
                }
            }
            if (it.paymentWebsiteType != null) {
                ApiEndPoint.PAYMENT_WEBSITE_TYPE = it.paymentWebsiteType!!
            }
        }
        config.packageInfo.externalUrls?.let {
            it.customPurchaseURL?.let { url -> TvCoreApplication.customPurchaseURL = url }
            it.adsTagUrl?.let { tagUrl ->
                if (tagUrl != "test.test") {
                    TvCoreApplication.adsTagURL = tagUrl
                }
            }
        }
        updateToken(config.packageInfo.accessKey, null)
        if (userPref.getAutoPlayConfig() == null) {
            userPref.setAutoPlay(config.packageInfo.general.autoPlay)
        }
        editor.putBoolean("autoplay", sharedPreferences.getBoolean("autoplay", true)).apply()
        Timber.d(config.toString())
        userPref.setTemplateId(config.packageInfo.templateId)
    }

    private fun generateQrCodePath(
        id: String,
        type: Int,
        purchaseLinkType: Int = 2,
        categoryId: String? = null,
        templateId: String? = null
    ): String {
        Timber.d("generateQrcodePath")
        val template =
            if (purchaseLinkType == 2 || templateId == null) _currentConfig?.packageInfo?.templateId else templateId
        val path = when (type) {
            0 -> "/watch-movies.html?videoId=${id}%26isOrderProcessing=true"
            1 -> "/play-audio.html?audioId=${id}%26isOrderProcessing=true"
            2 -> "/watch-movies.html?categoryId=${categoryId}%26videoId=${id}%26isOrderProcessing=true"
            3 -> "/play-audio.html?categoryId=${categoryId}%26videoId=${id}%26isOrderProcessing=true"
            else -> ""
        }
        var baseCMSURL = ApiEndPoint.BASE_CMS_URL + "clients/${userPref.getTemplateId()}"
        TvCoreApplication.sites[template]?.domain?.let {
            if (it.isNotEmpty()) {
                baseCMSURL = "https://$it"
            }
        }
        return "$baseCMSURL${path}"
    }
}
