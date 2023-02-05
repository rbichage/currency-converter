package com.currency.core_data.repository

import com.currency.core_network.api.CurrencyApi
import com.reuben.core_common.di.IODispatcher
import com.reuben.core_model.currency.CurrencySymbolsDTO
import com.reuben.core_model.currency.apiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrenciesRepositoryImpl @Inject constructor(
    private val currencyApi: CurrencyApi,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) : CurrenciesRepository {
    override suspend fun getCurrencySymbols(): CurrencySymbolsDTO =
        withContext(coroutineDispatcher) {
            return@withContext apiCall {
                currencyApi.getCurrencies()
            }
        }

    override suspend fun getConversionDates(
        startDate: String, endDate: String, baseCurrency: String, endCurrency: String
    ) = withContext(coroutineDispatcher) {
        return@withContext apiCall {
            currencyApi.getConversionForDateRange(
                baseCurrency = baseCurrency,
                convertedCurrency = endCurrency,
                startDate = startDate,
                endDate = endDate
            )
        }
    }

    override suspend fun getExchangeRatesForCurrency(baseCurrency: String) =
        withContext(coroutineDispatcher) {
            return@withContext apiCall {
                currencyApi.getExchangeRateForCurrency(
                    baseCurrency = baseCurrency
                )
            }
        }

}