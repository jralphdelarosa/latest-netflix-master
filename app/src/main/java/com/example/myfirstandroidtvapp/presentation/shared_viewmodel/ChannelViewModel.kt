package com.example.myfirstandroidtvapp.presentation.shared_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirstandroidtvapp.data.remote.dto.ChannelListResponse
import com.example.myfirstandroidtvapp.domain.repository.ContentsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject constructor(
    private val contentsRepository: ContentsRepository
) : ViewModel() {

    private val _channels = MutableStateFlow<ChannelListResponse?>(null)
    val channels: StateFlow<ChannelListResponse?> = _channels.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadChannels() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = contentsRepository.getChannels()
                Timber.tag("ChannelViewModel").d(response.toString())
                _channels.value = response
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}