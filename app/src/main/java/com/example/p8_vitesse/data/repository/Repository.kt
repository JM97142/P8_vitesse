package com.example.p8_vitesse.data.repository

import com.example.p8_vitesse.data.dao.ListCandidateDao
import com.example.p8_vitesse.data.entity.ListCandidateDto
import com.example.p8_vitesse.domain.model.Items
import kotlinx.coroutines.flow.first

class Repository(
    private val listCandidateDao: ListCandidateDao
) {
    suspend fun fetchAllCandidates(): List<Items> {
        val candidates = listCandidateDao.getAllCanddidates()
            .first()
            .map { Items.fromDto(it) }
        return candidates
    }

    suspend fun getCandidateById(id: Long): Items {
        return listCandidateDao.getCandidateById(id).let { Items.fromDto(it) }
    }

    suspend fun insertCandidate(candidate: ListCandidateDto) {
        listCandidateDao.insertCandidate(candidate)
    }
}