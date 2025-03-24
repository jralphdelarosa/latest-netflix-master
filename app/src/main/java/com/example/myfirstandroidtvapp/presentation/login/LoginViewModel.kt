package com.example.myfirstandroidtvapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirstandroidtvapp.data.remote.api.ApiResponse
import com.example.myfirstandroidtvapp.data.remote.dto.LoginResponse
import com.example.myfirstandroidtvapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: LoginResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(username: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            when (val response = userRepository.login(username, password)) {
                is ApiResponse.Success -> {
                    _loginState.value = LoginState.Success(response.data)
                }
                is ApiResponse.Error -> {
                    _loginState.value = LoginState.Error(response.message)
                }

                else -> {}
            }
        }
    }
}