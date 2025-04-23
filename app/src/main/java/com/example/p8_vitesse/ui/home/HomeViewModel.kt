package com.example.p8_vitesse.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p8_vitesse.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel (
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>("")
    val error: StateFlow<String?> = _error

    init {
        allCandidates()
    }

    fun allCandidates() {
        _loading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val errorMessage = "Aucun Candidat"
            _error.value = errorMessage
        }
    }
}