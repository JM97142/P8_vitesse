package com.example.p8_vitesse.data.repository

import com.example.p8_vitesse.data.dao.ListCandidateDao
import com.example.p8_vitesse.model.Items
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class Repository(private val listCandidateDao: ListCandidateDao) {

    suspend fun fetchAllCandidates(): List<Items> {
        val candidates = listCandidateDao.getAllCanddidates()
            .first()
            .map { Items.fromDto(it) }
        return candidates
    }
}