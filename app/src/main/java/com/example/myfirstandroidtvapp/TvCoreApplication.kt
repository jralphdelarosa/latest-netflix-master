package com.example.myfirstandroidtvapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.concurrent.Executors
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myfirstandroidtvapp.data.remote.dto.PublishedSite
import com.tencent.mmkv.BuildConfig
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.Enumeration
import java.util.UUID

/**
 * Created by John Ralph Dela Rosa on 3/23/2025.
 */

@HiltAndroidApp
class TvCoreApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Initialize MMKV for local storage
        val rootDir = MMKV.initialize(this)
        Timber.tag("tvapplogs").d("MMKV initialized at: $rootDir")

        if (debugApp) {
            Timber.plant(Timber.DebugTree())
        }

        initializeDeviceId()
        deviceType = if (packageManager.hasSystemFeature("amazon.hardware.fire_tv")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                idfa = getAmazonIFA()
            }
            "FIRETV"
        } else {
            getAndroidTVIFA(applicationContext)
            "ANDROIDTV"
        }
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidTVIFA(context: Context) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                idfa = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                isLat = false
            } catch (e: Exception) {
                e.printStackTrace()
                idfa = TvCoreApplication.deviceId
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun getAmazonIFA(): String {
        return try {
            val cr = contentResolver
            val limitAdTracking = Settings.Secure.getInt(cr, "limit_ad_tracking") != 0
            isLat = limitAdTracking
            if (isLat) {
                deviceId.toString()
            } else {
                Settings.Secure.getString(cr, "advertising_id") ?: deviceId.toString()
            }
        } catch (ex: Settings.SettingNotFoundException) {
            deviceId.toString()
        }
    }

    fun isUserLoggedIn(): LiveData<Boolean> {
        return isUserLoggedIn
    }

    companion object {
        lateinit var instance: TvCoreApplication
            private set

        var debugApp: Boolean = true
        var deviceId: String = ""
        var idfa: String = "00000000-0000-0000-0000-000000000000"
        var isLat: Boolean = false
        var deviceType: String = "ANDROIDTV"
        var userIp: String? = getLocalIpAddress()
        var adsTagURL: String = ""
        var videoPlayerURL: String = ""
        var audioPlayerURL: String = ""
        var liveStreamPlayerURL: String = ""
        var ppvURL: String = ""
        var subscriptionURL: String = ""
        var loginURL: String = ""
        var registerURL: String = ""
        var appStoreUrl: String = ""
        var customPurchaseURL: String = ""
        var LOGIN_URL: String = ""
        var sites: Map<String, PublishedSite> = emptyMap()
        var isUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
        var disableSubscription: Boolean = false
        var disableQR: Boolean = false
        var isLoginRequired: Boolean = false

        private fun getLocalIpAddress(): String? {
            return try {
                val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
                while (en.hasMoreElements()) {
                    val networkInterface: NetworkInterface = en.nextElement()
                    val enumerationIpAddress: Enumeration<InetAddress> =
                        networkInterface.inetAddresses
                    while (enumerationIpAddress.hasMoreElements()) {
                        val inetAddress: InetAddress = enumerationIpAddress.nextElement()
                        if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                            return inetAddress.hostAddress
                        }
                    }
                }
                null
            } catch (ex: SocketException) {
                ex.printStackTrace()
                null
            }
        }
    }

    private fun initializeDeviceId() {
        val mmkv = MMKV.defaultMMKV()
        val storedDeviceId: String? = mmkv.decodeString("device_id", null) // Ensure a default value
        TvCoreApplication.deviceId = (storedDeviceId ?: UUID.randomUUID().toString().also { newId ->
            mmkv.encode("device_id", newId)
        })
    }

}
