package com.reuben.feature_currency.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currency.core_data.domain.usecase.all_currencies.GetAllCurrenciesUseCase
import com.currency.core_data.domain.usecase.exchange_rate.ExchangeRateUseCase
import com.currency.core_data.domain.usecase.range.TimeSeriesUseCase
import com.reuben.core_model.api.TimeSeries
import com.reuben.core_model.currency.CurrenciesList
import com.reuben.core_model.currency.Currency
import com.reuben.core_model.currency.ExchangeRate
import com.reuben.core_model.currency.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
    private val exchangeRateUseCase: ExchangeRateUseCase,
    private val timeSeriesUseCase: TimeSeriesUseCase,
) : ViewModel() {

    private var _uiState: MutableStateFlow<CurrencyUIState> = MutableStateFlow(CurrencyUIState.Idle)
    val uiState = _uiState.asStateFlow()

    private val currencies = mutableSetOf<Currency>()
    private val exchangeRates = mutableSetOf<ExchangeRate>()

    fun getCurrencies() {
        viewModelScope.launch {
            getAllCurrenciesUseCase().collect { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.value = CurrencyUIState.Error(result.errorHolder.message)
                    }

                    Result.Loading -> {
                        _uiState.value = CurrencyUIState.Loading
                    }

                    is Result.Success -> {
                        currencies.addAll(result.data)
                        _uiState.value = CurrencyUIState.Currencies(
                            data = CurrenciesList(result.data)
                        )
                    }
                }
            }
        }
    }

    fun getExchangeRatesForCurrency(baseCurrency: String) {
        viewModelScope.launch {
            exchangeRateUseCase(
                baseCurrency = baseCurrency
            ).collect { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.value = CurrencyUIState.Error(result.errorHolder.message)
                    }

                    Result.Loading -> {
                        _uiState.value = CurrencyUIState.Loading
                    }

                    is Result.Success -> {
                        val data = result.data
                        exchangeRates.addAll(data)
                        _uiState.value = CurrencyUIState.ExchangeRates(result.data)
                    }
                }
            }
        }
    }

    fun getExchangeRateHistory(
        baseCurrency: String, destinationCurrency: String
    ) {
        viewModelScope.launch {
            timeSeriesUseCase(
                baseCurrency = baseCurrency, nextCurrency = destinationCurrency
            ).collect { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.value = CurrencyUIState.Error(result.errorHolder.message)
                    }

                    Result.Loading -> {
                        _uiState.value = CurrencyUIState.Loading
                    }

                    is Result.Success -> {
                        _uiState.value = CurrencyUIState.ExchangeRateHistory(result.data)
                    }
                }
            }
        }
    }

    fun resetUiState() {
        viewModelScope.launch {
            _uiState.value = CurrencyUIState.Idle
        }
    }
}

sealed interface CurrencyUIState {
    object Loading : CurrencyUIState
    object Idle : CurrencyUIState
    data class ExchangeRates(val data: List<ExchangeRate>) : CurrencyUIState
    data class ExchangeRateHistory(val data: List<TimeSeries>) : CurrencyUIState
    data class Currencies(val data: CurrenciesList) : CurrencyUIState
    data class Error(val message: String) : CurrencyUIState
}