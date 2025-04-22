package com.example.myfirstandroidtvapp.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirstandroidtvapp.TvCoreApplication
import com.example.myfirstandroidtvapp.data.remote.api.ApiResponse
import com.example.myfirstandroidtvapp.data.remote.dto.LoginResponse
import com.example.myfirstandroidtvapp.data.remote.dto.RegisterResponse
import com.example.myfirstandroidtvapp.data.remote.request.RegisterRequest
import com.example.myfirstandroidtvapp.data.remote.util.ApiResult
import com.example.myfirstandroidtvapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: LoginResponse) : LoginState()
    data class Error(val errorType: ErrorType, val message: String) : LoginState()

    object LoggOut : LoginState()
    object LogoutSuccess : LoginState()
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val registerResponse: RegisterResponse) : RegisterState()
    data class Error(val errorType: ErrorType, val message: String) : RegisterState()
}

enum class ErrorType {
    EMPTY_FIELDS,
    INVALID_CREDENTIALS,
    NETWORK_ERROR,
    UNKNOWN_ERROR
}


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _registerState = MutableStateFlow<ApiResult<RegisterResponse>>(ApiResult.Idle)
    val registerState: StateFlow<ApiResult<RegisterResponse>> = _registerState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            Timber.tag("LoginViewModel").d("Login started with email: $email")
            _loginState.value = LoginState.Loading

            when (val result = userRepository.login(email, password)) {
                is ApiResponse.Success -> {
                    Timber.tag("LoginViewModel").d("Login success: ${result.data} ${result.data.token?.access}")
                    _loginState.value = LoginState.Success(result.data)
                    TvCoreApplication.isUserLoggedIn.value = true
                }

                is ApiResponse.Error -> {
                    Timber.tag("LoginViewModel").e("Login failed: ${result.message}")

                    val errorType = when {
                        result.message.contains("empty", ignoreCase = true) -> ErrorType.EMPTY_FIELDS
                        result.message.contains("credentials", ignoreCase = true) -> ErrorType.INVALID_CREDENTIALS
                        result.message.contains("network", ignoreCase = true) -> ErrorType.NETWORK_ERROR
                        else -> ErrorType.UNKNOWN_ERROR
                    }

                    _loginState.value = LoginState.Error(errorType, result.message)
                }

                is ApiResponse.NetworkError -> {
                    Timber.tag("LoginViewModel").e("No internet connection")
                    _loginState.value = LoginState.Error( ErrorType.NETWORK_ERROR, "No internet connection. Please try again.")
                }

            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            Timber.tag("RegisterViewModel").d("Registration started with email: $email")
            _registerState.value = ApiResult.Loading

            when (val result = userRepository.register(RegisterRequest("", password, email))) {
                is ApiResult.Success -> {
                    Timber.tag("RegisterViewModel").d("Registration success: ${result.data}")
                    _registerState.value = ApiResult.Success(result.data)
                    TvCoreApplication.isUserLoggedIn.value = true
                }

                is ApiResult.Error -> {
                    Timber.tag("RegisterViewModel").e("Registration failed: ${result.message}")
                    _registerState.value = ApiResult.Error(result.message)
                }

                is ApiResult.Exception -> {
                    Timber.tag("RegisterViewModel").e("Registration exception: ${result.exception.message}")
                    _registerState.value = ApiResult.Exception(result.exception)
                }

                ApiResult.Loading -> {
                    _registerState.value = ApiResult.Loading
                }

                ApiResult.Idle -> {
                    _registerState.value = ApiResult.Idle
                }

            }
        }
    }

    fun registerStateSetIdle(){
        _registerState.value = ApiResult.Idle
    }

    fun logoutUser() {
        viewModelScope.launch {
            _loginState.value = LoginState.LoggOut
            userRepository.clearCurrentUserData()
            _loginState.value = LoginState.LogoutSuccess
        }
    }
}