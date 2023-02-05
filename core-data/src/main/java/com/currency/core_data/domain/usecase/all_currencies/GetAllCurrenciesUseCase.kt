package com.currency.core_data.domain.usecase.all_currencies

import com.currency.core_data.repository.CurrenciesRepository
import com.currency.core_data.util.convertCurrencySymbol
import com.reuben.core_common.di.IODispatcher
import com.reuben.core_model.currency.CurrencyException
import com.reuben.core_model.currency.asResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class GetAllCurrenciesUseCase @Inject constructor(
    private val currencyRepository: CurrenciesRepository,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {
    operator fun invoke() = flow {
        val symbolsDTO = currencyRepository.getCurrencySymbols()

        Timber.e("response $symbolsDTO")
        if (symbolsDTO.isSuccessful) {
            val currencies = symbolsDTO.currencies.convertCurrencySymbol()
            emit(currencies)
        } else {
            val errorMessage = symbolsDTO.error?.errorInfo ?: "Unable to complete request"
            throw CurrencyException(errorMessage)
        }
    }.catch {
        Timber.e("Currency error > $it")
        throw it
    }.flowOn(coroutineDispatcher).asResult()
}

