package com.reuben.feature_currency.ui.convert

import app.cash.turbine.test
import com.currency.core_data.domain.usecase.all_currencies.GetAllCurrenciesUseCase
import com.currency.core_data.domain.usecase.exchange_rate.ExchangeRateUseCase
import com.currency.core_data.domain.usecase.range.TimeSeriesUseCase
import com.currency.core_data.repository.CurrenciesRepository
import com.currency.core_data.repository.CurrenciesRepositoryImpl
import com.currency.core_data.util.convertCurrencySymbol
import com.currency.core_network.api.CurrencyApi
import com.currency.core_network.mock.MockFailureCurrencyApi
import com.currency.core_network.mock.MockSuccessfulCurrencyApi
import com.reuben.core_model.currency.CurrenciesList
import com.reuben.core_model.mock.mockCurrenciesDTO
import com.reuben.core_model.mock.mockGenericErrorDTO
import com.reuben.feature_currency.ui.CurrencyUIState
import com.reuben.feature_currency.ui.CurrencyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


@ExperimentalCoroutinesApi
class CurrencyViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var currenciesApi: CurrencyApi
    private val currencyRepository: CurrenciesRepository by lazy {
        CurrenciesRepositoryImpl(
            currenciesApi, testDispatcher
        )
    }

    private val getCurrenciesUseCase by lazy {
        GetAllCurrenciesUseCase(
            currencyRepository,
            testDispatcher
        )
    }
    private val exchangeRateUseCase by lazy {
        ExchangeRateUseCase(
            currencyRepository,
            testDispatcher
        )
    }

    private val timeSeriesUseCase by lazy {
        TimeSeriesUseCase(
            currencyRepository,
            testDispatcher
        )
    }

    val testCurrencies = CurrenciesList(mockCurrenciesDTO.currencies.convertCurrencySymbol())


    private val currencyViewModel: CurrencyViewModel by lazy {
        CurrencyViewModel(
            getAllCurrenciesUseCase = getCurrenciesUseCase,
            exchangeRateUseCase = exchangeRateUseCase,
            timeSeriesUseCase = timeSeriesUseCase
        )
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `test getting currencies is successful`() = runTest {
        currenciesApi = MockSuccessfulCurrencyApi(testDispatcher)

        currencyViewModel.getCurrencies()

        currencyViewModel.uiState.test {
            assertEquals(CurrencyUIState.Idle, awaitItem())
            assertEquals(CurrencyUIState.Loading, awaitItem())
            assertEquals(CurrencyUIState.Currencies(testCurrencies), awaitItem())
        }
    }

    @Test
    fun `test getting currencies is not successful`() = runTest {
        currenciesApi = MockFailureCurrencyApi(testDispatcher)

        currencyViewModel.getCurrencies()

        currencyViewModel.uiState.test {
            assertEquals(CurrencyUIState.Idle, awaitItem())
            assertEquals(CurrencyUIState.Loading, awaitItem())
            assertEquals(CurrencyUIState.Error(mockGenericErrorDTO.errorInfo.orEmpty()), awaitItem())
        }
    }

    @Test
    fun `test getting exchange rate is not successful`() = runTest {
        currenciesApi = MockFailureCurrencyApi(testDispatcher)

        currencyViewModel.getExchangeRatesForCurrency("USD")

        currencyViewModel.uiState.test {
            assertEquals(CurrencyUIState.Idle, awaitItem())
            assertEquals(CurrencyUIState.Loading, awaitItem())
            assertEquals(CurrencyUIState.Error(mockGenericErrorDTO.errorInfo.orEmpty()), awaitItem())
        }
    }

    @Test
    fun `test getting exchange rate history is not successful`() = runTest {
        currenciesApi = MockFailureCurrencyApi(testDispatcher)

        currencyViewModel.getExchangeRateHistory("USD", "KES")

        currencyViewModel.uiState.test {
            assertEquals(CurrencyUIState.Idle, awaitItem())
            assertEquals(CurrencyUIState.Loading, awaitItem())
            assertEquals(CurrencyUIState.Error(mockGenericErrorDTO.errorInfo.orEmpty()), awaitItem())
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}