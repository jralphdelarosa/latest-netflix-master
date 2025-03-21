package com.example.myfirstandroidtvapp.presentation.sections.home

import androidx.lifecycle.ViewModel
import com.example.myfirstandroidtvapp.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {


}