package com.example.myfirstandroidtvapp.data.remote.dto

data class UserProfile(
    val id: Int,
    val username: String,
    val email: String,
    val active: Boolean,
    val user_type: Int
)