package com.example.myfirstandroidtvapp.data.remote.dto

data class RegisterResponse(
    val token: TokenResponse,
    val profile: UserProfile
)