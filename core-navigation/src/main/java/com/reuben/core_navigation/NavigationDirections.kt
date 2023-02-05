package com.reuben.core_navigation

import androidx.navigation.NavController
import com.reuben.core_common.CurrencyType
import com.reuben.core_model.api.TimeSeries
import com.reuben.core_model.currency.CurrenciesList
import com.reuben.core_model.currency.ExchangeRate

interface NavigationDirections {
    fun navigateToCurrencies(
        navController: NavController,
        currenciesList: CurrenciesList,
        currencyType: CurrencyType
    )

    fun navigateToCurrencyDetails(
        navController: NavController,
        currenciesList: Array<ExchangeRate>,
        originCurrency: String,
        destinationCurrency: String,
        timeSeries : Array<TimeSeries>
    )
}