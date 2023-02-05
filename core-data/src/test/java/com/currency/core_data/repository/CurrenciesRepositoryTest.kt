package com.currency.core_data.repository

import com.currency.core_network.api.CurrencyApi
import com.currency.core_network.mock.MockFailureCurrencyApi
import com.currency.core_network.mock.MockSuccessfulCurrencyApi
import com.reuben.core_common.date.getDaysAgoDate
import com.reuben.core_common.date.getTodaysDate
import com.reuben.core_model.mock.mockCurrenciesDTO
import com.reuben.core_model.mock.mockExchangeRateDto
import com.reuben.core_model.mock.mockGenericErrorDTO
import com.reuben.core_model.mock.mockTimeSeriesDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.fail

@ExperimentalCoroutinesApi
class CurrenciesRepositoryTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var fakeCurrencyApi: CurrencyApi

    private val currenciesRepository by lazy {
        CurrenciesRepositoryImpl(fakeCurrencyApi, dispatcher)
    }

    private val testCurrenciesErrorDTO by lazy {
        mockCurrenciesDTO.copy(
            isSuccessful = false,
            error = mockGenericErrorDTO
        )
    }

    private val testExchangeRateErrorDTO by lazy {
        mockExchangeRateDto.copy(
            isSuccessful = false,
            error = mockGenericErrorDTO
        )
    }

    private val testTimeSeriesErrorDTO by lazy {
        mockTimeSeriesDTO.copy(
            isSuccessful = false,
            error = mockGenericErrorDTO
        )
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }


    @Test
    fun `test getting currencies returns a successful response`() = runTest {
        fakeCurrencyApi = MockSuccessfulCurrencyApi(dispatcher = dispatcher)

        val symbolsDTO = currenciesRepository.getCurrencySymbols()

        assertThat(symbolsDTO, `is`(mockCurrenciesDTO))
    }

    @Test
    fun `test getting currencies returns a failure`() = runTest {
        fakeCurrencyApi = MockFailureCurrencyApi(dispatcher = dispatcher)

        val symbolsDTO = currenciesRepository.getCurrencySymbols()

        if (symbolsDTO.isSuccessful) {
            fail("Request was successful")
        } else {
            assertThat(symbolsDTO, `is`(testCurrenciesErrorDTO))
            assertThat(symbolsDTO.error?.errorInfo, `is`(testCurrenciesErrorDTO.error?.errorInfo))
        }
    }

    @Test
    fun `test getting exchange rates returns a successful response`() = runTest {
        fakeCurrencyApi = MockSuccessfulCurrencyApi(dispatcher = dispatcher)

        val exchangeRateDto = currenciesRepository.getExchangeRatesForCurrency("USD")

        assertThat(exchangeRateDto, `is`(mockExchangeRateDto))
    }

    @Test
    fun `test getting exchange rates returns a failure`() = runTest {
        fakeCurrencyApi = MockFailureCurrencyApi(dispatcher = dispatcher)

        val exchangeRateDTO = currenciesRepository.getExchangeRatesForCurrency("")

        if (exchangeRateDTO.isSuccessful) {
            fail("Request was successful")
        } else {
            assertThat(exchangeRateDTO, `is`(testExchangeRateErrorDTO))
            assertThat(
                exchangeRateDTO.error?.errorInfo,
                `is`(testCurrenciesErrorDTO.error?.errorInfo)
            )
        }
    }

    @Test
    fun `test getting time series rates returns a successful response`() = runTest {
        fakeCurrencyApi = MockSuccessfulCurrencyApi(dispatcher = dispatcher)

        val timeSeriesDTO = currenciesRepository.getConversionDates(
            startDate = getTodaysDate(),
            endDate = getDaysAgoDate(3),
            baseCurrency = "USD",
            endCurrency = "KES"
        )

        assertThat(timeSeriesDTO, `is`(mockTimeSeriesDTO))
    }

    @Test
    fun `test getting time series rates returns a failure`() = runTest {
        fakeCurrencyApi = MockFailureCurrencyApi(dispatcher = dispatcher)

        val timeSeriesDTO = currenciesRepository.getConversionDates(
            startDate = getTodaysDate(),
            endDate = getDaysAgoDate(3),
            baseCurrency = "USD",
            endCurrency = "KES"
        )

        if (timeSeriesDTO.isSuccessful) {
            fail("Request was successful")
        } else {
            assertThat(timeSeriesDTO, `is`(testTimeSeriesErrorDTO))
            assertThat(
                timeSeriesDTO.error?.errorInfo,
                `is`(testTimeSeriesErrorDTO.error?.errorInfo)
            )
        }    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}