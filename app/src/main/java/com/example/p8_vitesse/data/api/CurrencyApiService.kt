package com.example.p8_vitesse.data.api

import retrofit2.http.GET

data class CurrencyResponse(
    val date: String,
    val gbp: Double // EUR → GBP
)

interface CurrencyApiService {
    @GET("eur/gbp.json")
    suspend fun getEuroToPoundRate(): CurrencyResponse
}