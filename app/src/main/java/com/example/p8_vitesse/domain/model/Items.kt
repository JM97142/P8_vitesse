package com.example.p8_vitesse.domain.model

import com.example.p8_vitesse.data.entity.ListCandidateDto

data class Items(
    val id: Long = 0,
    val lastName: String,
    val firstName: String,
    val phone: String,
    val email: String,
    val birthday: String,
    val wage: Double,
    val note: String,
    var picture: String?,
    var favorite: Boolean
) {
    companion object {
        fun fromDto(dto: ListCandidateDto) : Items {
            return Items(
                id = dto.id,
                lastName = dto.listName,
                firstName = dto.listFirstname,
                phone = dto.listPhone,
                email = dto.listEmail,
                birthday = dto.listBirthday,
                wage = dto.listWage,
                note = dto.listNote,
                favorite = dto.listFavorite,
                picture = dto.listPicture
            )
        }
    }

    fun toDto(): ListCandidateDto {
        return ListCandidateDto(
            id = id,
            listName = lastName,
            listFirstname = firstName,
            listPhone = phone,
            listEmail = email,
            listBirthday = birthday,
            listWage = wage,
            listNote = note,
            listFavorite = favorite,
            listPicture = picture
        )
    }
}