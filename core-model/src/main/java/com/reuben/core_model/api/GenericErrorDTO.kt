package com.reuben.core_model.api

import com.squareup.moshi.Json

data class GenericErrorDTO(
    @Json(name = "code") val errorCode: String,
    @Json(name = "type") val errorType: String,
    @Json(name = "info") val errorInfo:String?
)