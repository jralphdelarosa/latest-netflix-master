package com.example.myfirstandroidtvapp.di

import com.example.myfirstandroidtvapp.data.local.usersharedpref.UserSharedPref
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
class AuthInterceptor @Inject constructor(
    private val userPref: UserSharedPref
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = userPref.getUserAccessToken() ?: userPref.getAccessToken()

        val newRequest = originalRequest.newBuilder().apply {
            accessToken?.let {
                header("Authorization", it)
            }
        }.build()

        return chain.proceed(newRequest)
    }
}