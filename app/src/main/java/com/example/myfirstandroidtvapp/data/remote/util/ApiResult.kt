package com.example.myfirstandroidtvapp.data.remote.util

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}