package com.currency.core_data.domain.usecase.range

import com.currency.core_data.repository.CurrenciesRepository
import com.currency.core_data.util.convertTimeSeries
import com.reuben.core_common.date.getDaysAgoDate
import com.reuben.core_common.date.getTodaysDate
import com.reuben.core_common.di.IODispatcher
import com.reuben.core_model.currency.asResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TimeSeriesUseCase @Inject constructor(
    private val repository: CurrenciesRepository,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {
    operator fun invoke(
        baseCurrency: String, nextCurrency: String
    ) = flow {

        val startDate = getDaysAgoDate(3)
        val endDate = getTodaysDate()


        val dto = repository.getConversionDates(
            startDate = startDate,
            endDate = endDate,
            baseCurrency = baseCurrency,
            endCurrency = nextCurrency
        )

        if (dto.isSuccessful) {
            val rates = dto.rates.convertTimeSeries()
            emit(rates)
        } else {
            dto.error?.let {
                throw ConversionException(it.errorInfo.orEmpty())
            } ?: throw ConversionException("An error occurred")
        }
    }.catch {
        throw it
    }.flowOn(coroutineDispatcher).asResult()
}

data class ConversionException(override val message: String) : Exception(message)