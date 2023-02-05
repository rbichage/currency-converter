package com.reuben.core_model.currency


import android.os.Parcelable
import com.reuben.core_model.api.GenericErrorDTO
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

data class CurrencySymbolsDTO(
    @Json(name = "success") val isSuccessful: Boolean,
    @Json(name = "symbols")
    val currencies: Map<String, String>?,
    @Json(name = "error") val error: GenericErrorDTO?
)

@Parcelize
data class Currency(
    val currencyCode: String,
    val currencyName: String
) : Parcelable


@Parcelize
data class CurrenciesList(
    val currencies: List<Currency>
) : Parcelable


