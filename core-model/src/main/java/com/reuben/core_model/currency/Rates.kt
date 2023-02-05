package com.reuben.core_model.currency

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExchangeRate(
    val currencyCode: String,
    val currencyValue: Double
) : Parcelable

