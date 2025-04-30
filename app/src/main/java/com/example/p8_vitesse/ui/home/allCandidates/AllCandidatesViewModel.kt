package com.example.p8_vitesse.ui.home.allCandidates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p8_vitesse.domain.GetAllCandidateUseCase
import com.example.p8_vitesse.domain.model.Items
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllCandidatesViewModel @Inject constructor(
    private val allCandidateUseCase: GetAllCandidateUseCase
) : ViewModel() {

    private val _candidates = MutableStateFlow<List<Items>>(emptyList())
    val candidates: StateFlow<List<Items>> = _candidates.asStateFlow()

    init {
        allCandidates()
    }

    fun allCandidates() {
        viewModelScope.launch(Dispatchers.IO) {
            val allCandidates = allCandidateUseCase.execute()
            _candidates.value = allCandidates
        }
    }

    fun filterCandidate(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val candidates = allCandidateUseCase.execute()
            _candidates.update {
                candidates.filter { candidate ->
                    candidate.firstName.contains(query, ignoreCase = true) || candidate.lastName.contains(query, ignoreCase = true)
                }
            }
        }
    }
}