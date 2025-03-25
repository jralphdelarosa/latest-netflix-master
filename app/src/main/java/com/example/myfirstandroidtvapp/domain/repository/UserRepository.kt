package com.example.myfirstandroidtvapp.domain.repository

import com.example.myfirstandroidtvapp.data.remote.api.ApiResponse
import com.example.myfirstandroidtvapp.data.remote.dto.ConfigResponse
import com.example.myfirstandroidtvapp.data.remote.dto.CredentialResponse
import com.example.myfirstandroidtvapp.data.remote.dto.CustomDomainConfigResponse
import com.example.myfirstandroidtvapp.data.remote.dto.LoginResponse
import com.example.myfirstandroidtvapp.data.remote.dto.QrCodeResponse
import com.example.myfirstandroidtvapp.data.remote.util.ApiResult

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
interface UserRepository {
    suspend fun login(email: String, password: String): ApiResponse<LoginResponse>
    suspend fun refreshToken(): ApiResponse<LoginResponse>
    suspend fun getConfig(): ApiResponse<ConfigResponse>
    suspend fun loadCustomCMSConfig(): ApiResponse<CustomDomainConfigResponse>
    suspend fun generateQrCode(
        id: String, type: Int, purchaseLinkType: Int, categoryId: String?, templateId: String?
    ): ApiResponse<QrCodeResponse>

    suspend fun checkAutoLogin(): ApiResponse<CredentialResponse>
    fun updateToken(accessToken: String?, refreshToken: String?)
    fun updateUserToken(accessToken: String?, refreshToken: String?)
}
