package com.reuben.core_model.api


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

data class CurrencyTimeSeriesDTO(
    @Json(name = "base")
    val base: String?,
    @Json(name = "end_date")
    val endDate: String?,
    @Json(name = "rates")
    val rates: Map<String, Map<String, Double>>?,
    @Json(name = "start_date")
    val startDate: String?,
    @Json(name = "success")
    val isSuccessful: Boolean,
    @Json(name = "timeseries")
    val timeseries: Boolean?,
    @Json(name = "error")
    val error: GenericErrorDTO?
)
@Parcelize
data class TimeSeries(
    val date: String,
    val exchangeRate: String
) : Parcelable