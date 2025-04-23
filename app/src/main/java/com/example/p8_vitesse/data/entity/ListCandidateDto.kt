package com.example.p8_vitesse.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_candidate")
data class ListCandidateDto(
    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "candidate_id")
    var id: Long,

    @ColumnInfo(name = "candidate_name")
    var listName: String,

    @ColumnInfo(name = "candidate_firstname")
    var listFirstname: String,

    @ColumnInfo(name = "candidate_phone")
    var listPhone: String,

    @ColumnInfo(name = "candidate_email")
    var listEmail: String,

    @ColumnInfo(name = "candidate_birthday")
    var listBirthday: String,

    @ColumnInfo(name = "candidate_list_wage")
    var listWage: Double,

    @ColumnInfo(name = "candidate_note")
    var listNote: String,

    @ColumnInfo(name = "candidate_picture")
    var listPicture: String?,

    @ColumnInfo(name = "list_favorite")
    var listFavorite: Boolean = false
)