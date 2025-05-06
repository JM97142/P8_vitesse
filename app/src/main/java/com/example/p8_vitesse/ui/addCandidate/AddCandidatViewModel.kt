package com.example.p8_vitesse.ui.addCandidate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p8_vitesse.data.repository.Repository
import com.example.p8_vitesse.domain.model.Items
import kotlinx.coroutines.launch

class AddCandidatViewModel(
    private val repository: Repository
): ViewModel() {

    fun addCandidate(candidate: Items) {
        viewModelScope.launch {
            val candidateDto = candidate.toDto()
            repository.insertCandidate(candidateDto)
        }
    }
}