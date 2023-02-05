package com.currency.core_network.api

import com.currency.core_network.BuildConfig
import com.reuben.core_common.date.getDaysAgoDate
import com.reuben.core_common.date.getTodaysDate
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.test.assertNotNull

@ExperimentalCoroutinesApi
class NetworkCurrencyApiTest {
    private val testDispatcher = StandardTestDispatcher()

    private val server = MockWebServer()

    private lateinit var currencyApi: CurrencyApi

    @Before
    fun setup() {
        server.start()

        currencyApi = Retrofit.Builder()
            .baseUrl(server.url(""))
            .client(buildOkhttpClient())
            .addConverterFactory(MoshiConverterFactory.create(buildMoshi()))
            .build()
            .create(CurrencyApi::class.java)

        Dispatchers.setMain(testDispatcher)

    }

    @Test
    fun `test getting currencies isSuccessul`() = runTest {
        server.enqueue(MockResponse().setBody(testCurrenciesDTO))

        val result = currencyApi.getCurrencies()

        val request = server.takeRequest()
        val requestBody = request.body.readUtf8()

        assert(requestBody.isEmpty())
        assertNotNull(result)

        assert(result.isSuccessful)
        assert(result.currencies?.containsKey("AED") ?: false)
    }

    @Test
    fun `test getting time series with malformed dates throws an error`() = runTest {
        server.enqueue(MockResponse().setBody(testTimeSeriesErrorDTO))

        val result = currencyApi.getConversionForDateRange(
            baseCurrency = "USD",
            convertedCurrency = "KES",
            startDate = getDaysAgoDate(3),
            endDate = getTodaysDate()
        )

        val request = server.takeRequest()
        val requestBody = request.body.readUtf8()

        assert(requestBody.isEmpty())
        assertNotNull(result)
        assert(!result.isSuccessful)
        assertNotNull(result.error)
        assert(result.error?.errorType.contentEquals("invalid_time_frame"))
    }

    @After
    fun tearDown() {
        server.close()
        Dispatchers.resetMain()
    }


}

private fun buildOkhttpClient() = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .addInterceptor(ApiKeyInterceptor())
    .addNetworkInterceptor(HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }).build()

private fun buildMoshi() = Moshi
    .Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val testTimeSeriesSuccessfulDTO = """
    {
        "success": true,
        "timeseries": true,
        "start_date": "2023-02-01",
        "end_date": "2023-02-04",
        "base": "USD",
        "rates": {
            "2023-02-01": {
                "KES": 124.583254
            },
            "2023-02-02": {
                "KES": 124.696363
            },
            "2023-02-03": {
                "KES": 124.750385
            },
            "2023-02-04": {
                "KES": 125.809207
            }
        }
    }
""".trimIndent()

val testTimeSeriesErrorDTO = """
    {
        "success": false,
        "error": {
            "code": 504,
            "type": "invalid_time_frame",
            "info": "You have entered an invalid Time-Frame. [Required format: ...&start_date=YYYY-MM-DD&end_date=YYYY-MM-DD]"
        }
    }
""".trimIndent()
val testCurrenciesDTO = """
    {
    "success": true,
    "symbols": {
        "AED": "United Arab Emirates Dirham",
        "AFN": "Afghan Afghani",
        "ALL": "Albanian Lek",
        "AMD": "Armenian Dram",
        "ANG": "Netherlands Antillean Guilder",
        "AOA": "Angolan Kwanza",
        "ARS": "Argentine Peso",
        "AUD": "Australian Dollar",
        "AWG": "Aruban Florin",
        "AZN": "Azerbaijani Manat",
        "BAM": "Bosnia-Herzegovina Convertible Mark",
        "BBD": "Barbadian Dollar",
        "BDT": "Bangladeshi Taka",
        "BGN": "Bulgarian Lev",
        "BHD": "Bahraini Dinar",
        "BIF": "Burundian Franc",
        "BMD": "Bermudan Dollar",
        "BND": "Brunei Dollar",
        "BOB": "Bolivian Boliviano",
        "BRL": "Brazilian Real",
        "BSD": "Bahamian Dollar",
        "BTC": "Bitcoin",
        "BTN": "Bhutanese Ngultrum",
        "BWP": "Botswanan Pula",
        "BYN": "New Belarusian Ruble",
        "BYR": "Belarusian Ruble",
        "BZD": "Belize Dollar",
        "CAD": "Canadian Dollar",
        "CDF": "Congolese Franc",
        "CHF": "Swiss Franc",
        "CLF": "Chilean Unit of Account (UF)",
        "CLP": "Chilean Peso",
        "CNY": "Chinese Yuan",
        "COP": "Colombian Peso",
        "CRC": "Costa Rican Colón",
        "CUC": "Cuban Convertible Peso",
        "CUP": "Cuban Peso",
        "CVE": "Cape Verdean Escudo",
        "CZK": "Czech Republic Koruna",
        "DJF": "Djiboutian Franc",
        "DKK": "Danish Krone",
        "DOP": "Dominican Peso",
        "DZD": "Algerian Dinar",
        "EGP": "Egyptian Pound",
        "ERN": "Eritrean Nakfa",
        "ETB": "Ethiopian Birr",
        "EUR": "Euro",
        "FJD": "Fijian Dollar",
        "FKP": "Falkland Islands Pound",
        "GBP": "British Pound Sterling"
        }
        }
""".trimIndent()