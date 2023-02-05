package com.reuben.core_model.mock

import com.reuben.core_model.api.BaseCurrencyExchangeRatesDTO
import com.reuben.core_model.api.CurrencyTimeSeriesDTO
import com.reuben.core_model.api.GenericErrorDTO
import com.reuben.core_model.currency.CurrencySymbolsDTO

val mockGenericErrorDTO: GenericErrorDTO = GenericErrorDTO(
    errorCode = "100",
    errorInfo = "You have entered an invalid Time-Frame. [Required format: ...&start_date=YYYY-MM-DD&end_date=YYYY-MM-DD]",
    errorType = "invalid_time_frame"
)

val mockCurrenciesDTO = CurrencySymbolsDTO(
    isSuccessful = true, currencies = mapOf(
        "AED" to "United Arab Emirates Dirham",
        "AFN" to "Afghan Afghani, ALL=Albanian Lek",
        "AMD" to "Armenian Dram",
        "ANG" to "Netherlands Antillean Guilder",
        "AOA" to "Angolan Kwanza",
        "ARS" to "Argentine Peso",
    ),
    error = null
)

val mockExchangeRateDto = BaseCurrencyExchangeRatesDTO(
    base = "USD",
    date = "2023-02-01",
    rates = mapOf(
        "AED" to "3.673104",
        "AFN" to "90.511151",
        "ALL" to "107.290945",
        "AMD" to "399.879946",
        "ANG" to "1.818996",
        "AOA" to "504.408041",
        "ARS" to "187.42836"),
    isSuccessful = true,
    timestamp = System.currentTimeMillis(),
    error = null
)

val mockTimeSeriesDTO = CurrencyTimeSeriesDTO(
    base = "USD",
    endDate = "2023-02-05",
    startDate = "2023-02-05",
    isSuccessful = true,
    error = null,
    timeseries = true,
    rates = mapOf(
        "2023-02-02" to mapOf(pair = Pair("KES", 124.696363)),
        "2023-02-03" to mapOf(pair = Pair("KES", 124.676363)),
        "2023-02-04" to mapOf(pair = Pair("KES", 124.996363)),
    )
)



