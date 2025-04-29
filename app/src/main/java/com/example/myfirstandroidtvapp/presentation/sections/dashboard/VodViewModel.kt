package com.example.myfirstandroidtvapp.presentation.sections.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirstandroidtvapp.data.remote.dto.VodCategoryResponse
import com.example.myfirstandroidtvapp.data.remote.dto.VodVideo
import com.example.myfirstandroidtvapp.domain.repository.ContentsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VodViewModel @Inject constructor(
    private val contentsRepository: ContentsRepository
) : ViewModel() {

    private val _vodCategories = MutableStateFlow<Result<VodCategoryResponse>?>(null)
    val vodCategories: StateFlow<Result<VodCategoryResponse>?> = _vodCategories

    var selectedMovie: VodVideo? by mutableStateOf(null)

    fun fetchVodCategories() {
        viewModelScope.launch {
            _vodCategories.value = contentsRepository.fetchVodCategories()
        }
    }
}