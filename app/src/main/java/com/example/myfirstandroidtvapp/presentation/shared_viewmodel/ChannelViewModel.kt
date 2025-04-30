package com.example.myfirstandroidtvapp.presentation.shared_viewmodel

import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirstandroidtvapp.data.remote.dto.ChannelListResponse
import com.example.myfirstandroidtvapp.data.remote.dto.ChannelResponse
import com.example.myfirstandroidtvapp.domain.repository.ContentsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class ChannelUiModel(
    val title: String,
    val thumbnail: String,
    val videos: List<ProgramUiModel>,
    val position: Int
)

data class ProgramUiModel(
    val id: String,
    val title: String,
    val duration: String,
    val width: Dp,
    val position: Int
)

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

    private val _uiChannels = MutableStateFlow<List<ChannelUiModel>>(emptyList())
    val uiChannels: StateFlow<List<ChannelUiModel>> = _uiChannels.asStateFlow()

    fun loadChannels() {
        viewModelScope.launch {
            try {
                val response = contentsRepository.getChannels()
                val mapped = mapChannelsToUiModel(response.results)
                Timber.tag("ChannelViewModel").d(response.toString())
                _uiChannels.value = mapped
            } catch (e: Exception) {
                // Handle error, like logging or showing a message
            }
        }
    }

    private fun mapChannelsToUiModel(channels: List<ChannelResponse>): List<ChannelUiModel> {
        val thirtyMinuteWidth = 300f
        return channels.map { channel ->
            ChannelUiModel(
                title = channel.title ?: "",
                thumbnail = channel.thumbnail ?: "",
                videos = channel.playlists?.flatMap { playlist ->
                    playlist.videosOrder.orEmpty().mapNotNull { video ->
                        video.video?.let {
                            val duration = it.duration ?: return@let null
                            val minutes = parseDurationToMinutes(duration)
                            ProgramUiModel(
                                id = it.uuid ?: java.util.UUID.randomUUID().toString(),
                                title = it.title ?: "Untitled",
                                duration = duration,
                                width = Dp((minutes / 30f) * thirtyMinuteWidth),
                                position = video.sortOrder
                            )
                        }
                    }
                } ?: emptyList(),
                position = channel.position
            )
        }
    }

    private fun parseDurationToMinutes(duration: String?): Float {
        if (duration == null) return 0f

        return try {
            val parts = duration.split(":")
            if (parts.size != 3) return 0f

            val hours = parts[0].toFloatOrNull() ?: 0f
            val minutes = parts[1].toFloatOrNull() ?: 0f
            val seconds = parts[2].toFloatOrNull() ?: 0f

            hours * 60f + minutes + seconds / 60f
        } catch (e: Exception) {
            0f
        }
    }
}