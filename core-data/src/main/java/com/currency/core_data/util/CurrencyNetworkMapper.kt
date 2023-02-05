package com.currency.core_data.util

import com.reuben.core_model.api.TimeSeries
import com.reuben.core_model.currency.Currency
import com.reuben.core_model.currency.ExchangeRate


fun Map<String, String>?.convertCurrencySymbol() = this?.keys?.map {
    val valueOfKey = this[it]
    return@map Currency(
        currencyCode = it, currencyName = valueOfKey.orEmpty()
    )
} ?: emptyList()


fun Map<String, String>?.convertExchangeRate() = this?.keys?.map {
    val valueOfKey = this.getValue(it).toDouble()
    return@map ExchangeRate(
        currencyCode = it, currencyValue = valueOfKey
    )
} ?: emptyList()

fun Map<String, Map<String, Double>>?.convertTimeSeries() = this?.keys?.map {
    val exchangeRate = this.getValue(it)

    val currency = exchangeRate.keys.firstOrNull().orEmpty()
    val value = exchangeRate.values.firstOrNull() ?: 0.00
    return@map TimeSeries(
        date = it,
        exchangeRate = "$value"
    )
} ?: emptyList()


