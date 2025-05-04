package com.example.p8_vitesse.ui.candidateDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.p8_vitesse.domain.model.Items
import com.example.p8_vitesse.domain.usecase.GetCandidateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CandidateDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCandidateUseCase: GetCandidateUseCase
): ViewModel() {

    private val candidateId: Long = savedStateHandle[CandidateDetailActivity.EXTRA_CANDIDATE_ID] ?: -1L

    private val _candidate = MutableLiveData<Items?>()
    val candidate: LiveData<Items?> = _candidate

    init {
        loadCandidate()
    }

    private fun loadCandidate() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getCandidateUseCase.execute(candidateId)
            _candidate.postValue(result)
        }
    }
}