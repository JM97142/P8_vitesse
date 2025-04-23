package com.example.p8_vitesse.model

import com.example.p8_vitesse.data.entity.ListCandidateDto

data class Items(
    val id: Long = 0,
    val name: String,
    val firstname: String,
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
               name = dto.listName,
               firstname = dto.listFirstname,
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
            id = this.id,
            listName = this.name,
            listFirstname = this.firstname,
            listPhone = this.phone,
            listEmail = this.email,
            listBirthday = this.birthday,
            listWage = this.wage,
            listNote = this.note,
            listFavorite = this.favorite,
            listPicture = this.picture


        )
    }
}