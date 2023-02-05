package com.currency.core_data.repository

import com.reuben.core_model.api.BaseCurrencyExchangeRatesDTO
import com.reuben.core_model.api.CurrencyTimeSeriesDTO
import com.reuben.core_model.currency.CurrencySymbolsDTO


interface CurrenciesRepository {
    suspend fun getCurrencySymbols(): CurrencySymbolsDTO
    suspend fun getConversionDates(
        startDate: String, endDate: String, baseCurrency: String, endCurrency: String
    ): CurrencyTimeSeriesDTO

    suspend fun getExchangeRatesForCurrency(
        baseCurrency: String
    ): BaseCurrencyExchangeRatesDTO
}

