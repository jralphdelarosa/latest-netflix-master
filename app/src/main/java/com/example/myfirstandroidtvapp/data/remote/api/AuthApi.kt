package com.example.myfirstandroidtvapp.data.remote.api

import com.example.myfirstandroidtvapp.data.remote.dto.AppContentPlanResponse
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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
// Sealed class for API responses
sealed class ApiResponse<out T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Error(val message: String) : ApiResponse<Nothing>()
    object NetworkError : ApiResponse<Nothing>()
}

interface AuthApi {
    @POST("clients/auth/{tenantId}/login/")
    suspend fun login(
        @Path("tenantId") tenantId: String,
        @Body request: LoginRequest
    ): LoginResponse

    @POST("clients/auth/{tenant_id}/register/")
    suspend fun register(
        @Path("tenant_id") tenantId: String,
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @POST("auth/refresh/")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): ApiResponse<LoginResponse>

    @GET("app/configs")
    suspend fun getServerConfig(): ApiResponse<ConfigResponse>

    @GET
    suspend fun loadCustomDomainConfig(@Url url: String): ApiResponse<CustomDomainConfigResponse>

    @GET
    suspend fun loadAppContentPlans(@Url url: String): ApiResponse<AppContentPlanResponse>

    @POST("billing/order/qr-code")
    suspend fun generateQrCode(
        @Query("data") generateUrl: String
    ): ApiResponse<QrCodeResponse>

    @POST("clients/auth/auto-login/")
    suspend fun checkAutoLogin(@Body request: AutoLoginRequest): ApiResponse<CredentialResponse>
}