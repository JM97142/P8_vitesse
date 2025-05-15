package com.example.p8_vitesse.ui.home.candidatesFragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p8_vitesse.data.repository.Repository
import com.example.p8_vitesse.domain.usecase.GetAllCandidateUseCase
import com.example.p8_vitesse.domain.model.Items
import com.example.p8_vitesse.domain.usecase.DeleteCandidateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllCandidatesViewModel @Inject constructor(
    private val allCandidateUseCase: GetAllCandidateUseCase,
    private val deleteCandidateUseCase: DeleteCandidateUseCase,
    private val repository: Repository
) : ViewModel() {

    private val _candidates = MutableStateFlow<List<Items>>(emptyList())

    val candidates: StateFlow<List<Items>> = repository.fetchAllCandidatesFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _query = MutableStateFlow("") // requête de recherche

    init {
        allCandidates()
    }

    fun allCandidates() {
        viewModelScope.launch(Dispatchers.IO) {
            val allCandidates = allCandidateUseCase.execute()
            _candidates.value = allCandidates
        }
    }

    // Liste filtrée
    val filtered: StateFlow<List<Items>> = _query
        .flatMapLatest { q ->
            candidates.map { list ->
                if (q.isBlank()) list
                else list.filter {
                    it.firstName.contains(q, true) || it.lastName.contains(q, true)
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setQuery(text: String) {
        _query.value = text
    }

    fun deleteCandidate(id: Long) {
        viewModelScope.launch {
            deleteCandidateUseCase.execute(id)
        }
    }
}