package com.example.p8_vitesse.ui.editCandidate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p8_vitesse.data.repository.Repository
import com.example.p8_vitesse.domain.model.Items
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCandidateViewModel @Inject constructor(
    private val repository: Repository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    // Flow interne : représente l'état actuel du candidat
    private val _candidate = MutableStateFlow<Items?>(null)
    // Flow public exposé à l'UI
    val candidate: StateFlow<Items?> = _candidate.asStateFlow()

    // Charge un candidat depuis la base via son ID
    fun loadCandidate(id: Long) {
        viewModelScope.launch(ioDispatcher) {
            val result = repository.getCandidateById(id)
            _candidate.value = result
        }
    }

    // Met à jour un candidat dans la base
    fun updateCandidate(candidate: Items) {
        viewModelScope.launch(ioDispatcher) {
            repository.updateCandidate(candidate)
        }
    }
}