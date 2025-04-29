package com.example.myfirstandroidtvapp.di

import android.content.Context
import android.content.SharedPreferences
import com.example.myfirstandroidtvapp.data.local.usersharedpref.UserSharedPref
import com.example.myfirstandroidtvapp.data.local.usersharedpref.UserSharedPrefImpl
import com.example.myfirstandroidtvapp.data.remote.ApiConstants
import com.example.myfirstandroidtvapp.data.remote.ApiConstants.API_BASE
import com.example.myfirstandroidtvapp.data.remote.api.V3Api
import com.example.myfirstandroidtvapp.data.remote.api.V1Api
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Logs full request & response body
        }
    }

    // Provide Gson instance for JSON parsing
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().serializeNulls().create() // Ensures null fields are included
    }

    // Provide SharedPreferences
    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // Provide UserPref implementation (Handles tokens)
    @Provides
    @Singleton
    fun provideUserSharedPref(context: Context): UserSharedPref =
        UserSharedPrefImpl(context, "user_prefs")

    // Interceptor for Authentication (Injects Tokens)
    @Provides
    @Singleton
    fun provideAuthInterceptor(userPref: UserSharedPref): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()

            userPref.getUserAccessToken()?.let { token ->
                Timber.tag("tvapplogs").d("access token: $token")
                request.addHeader("Authorization", "Bearer $token")
            }

            chain.proceed(request.build())
        }
    }

    // Provide OkHttpClient with Interceptor
    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    // Provide Retrofit instance
    @Provides
    @Singleton
    @Named("authRetrofit")
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.API_V3_BASE) // Update with your base URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @Named("vodRetrofit")
    fun provideVodRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_BASE)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // Provide AuthApi
    @Provides
    @Singleton
    fun provideAuthApi(@Named("authRetrofit")retrofit: Retrofit): V3Api =
        retrofit.create(V3Api::class.java)

    @Provides
    @Singleton
    fun provideVodApi(@Named("vodRetrofit") retrofit: Retrofit): V1Api =
        retrofit.create(V1Api::class.java)

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
}