package com.example.p8_vitesse.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.p8_vitesse.data.entity.listCandidateDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ListCandidateDao {
    @Insert
    suspend fun insertCandidate(listCandidateDto: listCandidateDto): Long

    @Query("SELECT * from list_candidate")
    fun getAllCanddidates(): Flow<List<listCandidateDto>>
}