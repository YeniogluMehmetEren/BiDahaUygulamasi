package com.example.emptyviewsactivity.API.Currency

import com.example.emptyviewsactivity.API.ExchangeRateResponse
import retrofit2.Call
import retrofit2.http.GET

interface CurrencyService {
    @GET("v4/latest/TRY")
    fun getRates(): Call<ExchangeRateResponse>
}