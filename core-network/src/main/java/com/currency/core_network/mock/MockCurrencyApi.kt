package com.currency.core_network.mock

import com.currency.core_network.api.CurrencyApi
import com.reuben.core_common.di.IODispatcher
import com.reuben.core_model.api.BaseCurrencyExchangeRatesDTO
import com.reuben.core_model.api.CurrencyTimeSeriesDTO
import com.reuben.core_model.currency.CurrencySymbolsDTO
import com.reuben.core_model.mock.mockCurrenciesDTO
import com.reuben.core_model.mock.mockExchangeRateDto
import com.reuben.core_model.mock.mockGenericErrorDTO
import com.reuben.core_model.mock.mockTimeSeriesDTO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MockSuccessfulCurrencyApi @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : CurrencyApi {
    override suspend fun getCurrencies(): CurrencySymbolsDTO = withContext(dispatcher) {
        delay(MOCK_DELAY)
        return@withContext mockCurrenciesDTO
    }

    override suspend fun getConversionForDateRange(
        baseCurrency: String,
        convertedCurrency: String,
        startDate: String,
        endDate: String
    ): CurrencyTimeSeriesDTO = withContext(dispatcher) {
        delay(MOCK_DELAY)
        return@withContext mockTimeSeriesDTO
    }

    override suspend fun getExchangeRateForCurrency(baseCurrency: String): BaseCurrencyExchangeRatesDTO =
        withContext(dispatcher) {
            delay(MOCK_DELAY)
            return@withContext mockExchangeRateDto
        }
}

class MockFailureCurrencyApi @Inject constructor(
    private val dispatcher: CoroutineDispatcher
) : CurrencyApi {
    override suspend fun getCurrencies(): CurrencySymbolsDTO = withContext(dispatcher) {
        delay(MOCK_DELAY)
        return@withContext mockCurrenciesDTO.copy(
            isSuccessful = false,
            error = mockGenericErrorDTO
        )
    }

    override suspend fun getConversionForDateRange(
        baseCurrency: String,
        convertedCurrency: String,
        startDate: String,
        endDate: String
    ): CurrencyTimeSeriesDTO = withContext(dispatcher) {
        delay(MOCK_DELAY)
        return@withContext mockTimeSeriesDTO.copy(
            isSuccessful = false,
            error = mockGenericErrorDTO
        )
    }

    override suspend fun getExchangeRateForCurrency(baseCurrency: String): BaseCurrencyExchangeRatesDTO =
        withContext(dispatcher) {
            delay(MOCK_DELAY)
            return@withContext mockExchangeRateDto.copy(
                isSuccessful = false,
                error = mockGenericErrorDTO
            )
        }
}

const val MOCK_DELAY = 1000L