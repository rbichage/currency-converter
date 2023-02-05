package com.currency.core_network.api

import com.reuben.core_model.api.BaseCurrencyExchangeRatesDTO
import com.reuben.core_model.api.CurrencyTimeSeriesDTO
import com.reuben.core_model.currency.CurrencySymbolsDTO
import retrofit2.http.GET
import retrofit2.http.Query


interface CurrencyApi {

    @GET("symbols")
    suspend fun getCurrencies(): CurrencySymbolsDTO

    @GET("timeseries")
    suspend fun getConversionForDateRange(
        @Query("base") baseCurrency: String,
        @Query("symbols") convertedCurrency: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ) : CurrencyTimeSeriesDTO

    @GET("latest")
    suspend fun getExchangeRateForCurrency(
        @Query("base") baseCurrency: String
    ): BaseCurrencyExchangeRatesDTO
}
