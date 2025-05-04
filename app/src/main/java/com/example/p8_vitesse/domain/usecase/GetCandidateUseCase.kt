package com.example.p8_vitesse.domain.usecase

import com.example.p8_vitesse.data.repository.Repository
import com.example.p8_vitesse.domain.model.Items
import javax.inject.Inject

class GetCandidateUseCase @Inject constructor(private val repository: Repository){
    suspend fun execute(id : Long) : Items {
        return repository.getCandidateById(id)
    }
}