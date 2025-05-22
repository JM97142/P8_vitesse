package com.example.p8_vitesse.data.api

import retrofit2.http.GET

data class CurrencyResponse(
    val date: String,
    val eur: Map<String, Double>
)

interface CurrencyApiService {
    @GET("currencies/eur.json")
    suspend fun getEuroRates(): CurrencyResponse
}