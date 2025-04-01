package com.example.myfirstandroidtvapp.presentation.sections.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirstandroidtvapp.data.remote.dto.VodCategoryResponse
import com.example.myfirstandroidtvapp.domain.repository.VodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VodViewModel @Inject constructor(
    private val vodRepository: VodRepository
) : ViewModel() {

    private val _vodCategories = MutableStateFlow<Result<VodCategoryResponse>?>(null)
    val vodCategories: StateFlow<Result<VodCategoryResponse>?> = _vodCategories

    fun fetchVodCategories() {
        viewModelScope.launch {
            _vodCategories.value = vodRepository.fetchVodCategories()
            Timber.tag("vod response").d("VodViewModel")
        }
    }
}