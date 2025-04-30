package com.example.p8_vitesse.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.p8_vitesse.data.entity.ListCandidateDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ListCandidateDao {
    @Insert
    suspend fun insertCandidate(listCandidateDto: ListCandidateDto): Long

    @Query("SELECT * from list_candidate")
    fun getAllCanddidates(): Flow<List<ListCandidateDto>>

    @Query("SELECT * from list_candidate WHERE candidate_id = :id")
    suspend fun getCandidateById(id: Long): ListCandidateDto

    @Query("DELETE FROM list_candidate WHERE candidate_id = :id")
    suspend fun deleteCandidateById(id: Long)
}