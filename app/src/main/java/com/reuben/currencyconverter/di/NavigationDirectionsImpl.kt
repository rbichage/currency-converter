package com.reuben.currencyconverter.di

import androidx.navigation.NavController
import com.reuben.core_common.CurrencyType
import com.reuben.core_model.api.TimeSeries
import com.reuben.core_model.currency.CurrenciesList
import com.reuben.core_model.currency.ExchangeRate
import com.reuben.core_navigation.NavigationDirections
import com.reuben.feature_currency.ui.convert.ConvertCurrencyFragmentDirections
import javax.inject.Inject

class NavigationDirectionsImpl @Inject constructor() : NavigationDirections {

    override fun navigateToCurrencies(
        navController: NavController, currenciesList: CurrenciesList, currencyType: CurrencyType
    ) {
        navController.navigate(
            ConvertCurrencyFragmentDirections.toCurrencies(
                currenciesList, currencyType
            )
        )
    }

    override fun navigateToCurrencyDetails(
        navController: NavController,
        currenciesList: Array<ExchangeRate>,
        originCurrency: String,
        destinationCurrency: String,
        timeSeries: Array<TimeSeries>
    ) {
        navController.navigate(
            ConvertCurrencyFragmentDirections.toCurrencyDetails(
                originCurrency, destinationCurrency, timeSeries, currenciesList
            )
        )
    }
}