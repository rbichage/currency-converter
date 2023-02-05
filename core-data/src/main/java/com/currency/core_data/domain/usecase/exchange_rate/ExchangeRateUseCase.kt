package com.currency.core_data.domain.usecase.exchange_rate

import com.currency.core_data.repository.CurrenciesRepository
import com.currency.core_data.util.convertExchangeRate
import com.reuben.core_common.di.IODispatcher
import com.reuben.core_model.currency.CurrencyException
import com.reuben.core_model.currency.asResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class ExchangeRateUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    operator fun invoke(baseCurrency: String) = flow {
        val response = currenciesRepository.getExchangeRatesForCurrency(baseCurrency)

        if (response.isSuccessful) {
            val rates = response.rates.convertExchangeRate()
            emit(rates)
        } else {
            val errorMessage = response.error?.errorInfo.orEmpty()
            throw CurrencyException(errorMessage)
        }
    }.catch {
        Timber.e("exception: $it")
        throw it
    }.flowOn(coroutineDispatcher).asResult()
}