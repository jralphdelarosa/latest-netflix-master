package com.example.myfirstandroidtvapp.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirstandroidtvapp.data.remote.api.ApiResponse
import com.example.myfirstandroidtvapp.data.remote.dto.LoginResponse
import com.example.myfirstandroidtvapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
sealed class    LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: LoginResponse) : LoginState()
    data class Error(val errorType: ErrorType, val message: String) : LoginState()
}

enum class ErrorType {
    EMPTY_FIELDS,
    INVALID_CREDENTIALS,
    NETWORK_ERROR,
    UNKNOWN_ERROR
}


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: UserRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            Timber.tag("LoginViewModel").d("Login started with email: $email")
            _loginState.value = LoginState.Loading

            when (val result = authRepository.login(email, password)) {
                is ApiResponse.Success -> {
                    Timber.tag("LoginViewModel").d("Login success: ${result.data}")
                    _loginState.value = LoginState.Success(result.data)
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
}