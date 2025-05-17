package com.example.p8_vitesse.ui.candidateDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.p8_vitesse.data.repository.Repository
import com.example.p8_vitesse.domain.model.Items
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CandidateDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, // Récupère les arguments de l’intent (candidateId)
    private val repository: Repository
): ViewModel() {
    // Récupère l'ID du candidat depuis l'intent (via SavedStateHandle)
    private val candidateId: Long = savedStateHandle[CandidateDetailActivity.EXTRA_CANDIDATE_ID] ?: -1L

    // Flow interne : représente l'état actuel du candidat
    private val _candidate = MutableStateFlow<Items?>(null)
    // Flow public exposé à l'UI
    val candidate: StateFlow<Items?> = _candidate.asStateFlow()

    init {
        loadCandidate()
    }

    // Charge les données du candidat
    private fun loadCandidate() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getCandidateById(candidateId)
            _candidate.value = result
        }
    }

    // Rafraîchissement depuis la base de données
    fun refreshCandidate() {
        loadCandidate()
    }

    // Gestion de l’état "favori" du candidat
    fun toggleFavorite() {
        val current = _candidate.value ?: return
        val updated = current.copy(favorite = !current.favorite)

        _candidate.value = updated

        viewModelScope.launch {
            repository.updateCandidate(updated)
        }
    }

    // Supprime le candidat
    fun deleteCandidate(id: Long) {
        viewModelScope.launch {
            repository.deleteCandidateById(id)
        }
    }
}