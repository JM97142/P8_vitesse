package com.example.p8_vitesse.domain

import com.example.p8_vitesse.data.repository.Repository
import com.example.p8_vitesse.domain.model.Items
import javax.inject.Inject

class GetAllCandidateUseCase@Inject constructor(private val repository: Repository) {
    suspend fun execute() : List<Items> {
        return repository.fetchAllCandidates()
    }
}