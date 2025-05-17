package com.example.p8_vitesse.ui.editCandidate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p8_vitesse.data.repository.Repository
import com.example.p8_vitesse.domain.model.Items
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCandidateViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _candidate = MutableLiveData<Items?>() // Stock le candidat chargé
    val candidate: LiveData<Items?> = _candidate

    // Charge un candidat depuis la base via son ID
    fun loadCandidate(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getCandidateById(id)
            _candidate.postValue(result)
        }
    }

    // Met à jour un candidat dans la base
    fun updateCandidate(candidate: Items) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCandidate(candidate)
        }
    }
}