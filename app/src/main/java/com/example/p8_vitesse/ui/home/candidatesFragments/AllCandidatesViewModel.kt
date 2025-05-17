package com.example.p8_vitesse.ui.home.candidatesFragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p8_vitesse.data.repository.Repository
import com.example.p8_vitesse.domain.model.Items
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllCandidatesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    // Flux contenant tous les candidats, partagé avec un état initial vide
    val candidates: StateFlow<List<Items>> = repository.fetchAllCandidatesFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _query = MutableStateFlow("") // requête de recherche

    // Liste filtrée selon la recherche (nom/prénom)
    @OptIn(ExperimentalCoroutinesApi::class)
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

    // Mise à jour de la recherche
    fun setQuery(text: String) {
        _query.value = text
    }
    // Supprime un candidat via son Id
    fun deleteCandidate(id: Long) {
        viewModelScope.launch {
            repository.deleteCandidateById(id)
        }
    }
}