package com.example.myfirstandroidtvapp.presentation.sections.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val context: Application
) : AndroidViewModel(context) {

    fun clearAppCache() {
        viewModelScope.launch {
            try {
                val dir = context.cacheDir
                dir?.deleteRecursively()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}