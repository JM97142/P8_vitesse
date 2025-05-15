package com.example.p8_vitesse.data.repository

import com.example.p8_vitesse.data.dao.ListCandidateDao
import com.example.p8_vitesse.data.entity.ListCandidateDto
import com.example.p8_vitesse.domain.model.Items
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class Repository @Inject constructor(
    private val listCandidateDao: ListCandidateDao
) {
    suspend fun fetchAllCandidates(): List<Items> {
        val candidates = listCandidateDao.getAllCanddidates()
            .first()
            .map { Items.fromDto(it) }
        return candidates
    }

    fun fetchAllCandidatesFlow(): Flow<List<Items>> {
        return listCandidateDao.getAllCanddidates()
            .map { list -> list.map { Items.fromDto(it) } }
    }

    suspend fun getCandidateById(id: Long): Items? {
        return listCandidateDao.getCandidateById(id)?.let { Items.fromDto(it) }
    }

    suspend fun insertCandidate(candidate: ListCandidateDto) {
        listCandidateDao.insertCandidate(candidate)
    }

    suspend fun deleteCandidateById(id: Long) {
        listCandidateDao.deleteCandidateById(id)
    }

    suspend fun updateCandidate(candidate: Items) {
        listCandidateDao.updateCandidate(candidate.toDto())
    }
}