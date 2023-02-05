package com.reuben.core_model.api


import com.squareup.moshi.Json

data class BaseCurrencyExchangeRatesDTO(
    @Json(name = "base") val base: String,
    @Json(name = "date") val date: String,
    @Json(name = "rates") val rates: Map<String, String>?,
    @Json(name = "success") val isSuccessful: Boolean,
    @Json(name = "timestamp") val timestamp: Long,
    @Json(name = "error") val error: GenericErrorDTO?
)