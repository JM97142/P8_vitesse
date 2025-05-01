package com.example.p8_vitesse.ui.home.candidatesFragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p8_vitesse.domain.usecase.GetAllCandidateUseCase
import com.example.p8_vitesse.domain.model.Items
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
}