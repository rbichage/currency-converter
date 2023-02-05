package com.reuben.core_common.date

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

// 2023-02-01
fun getYYYMMddDateFormatter() = SimpleDateFormat("yyyy-MM-dd", Locale.US)


fun getDaysAgoDate(daysAgo: Long): String {
    val lastDaysInMillis = TimeUnit.DAYS.toMillis(daysAgo)

    val date = Date(Date().time - lastDaysInMillis)

    return getYYYMMddDateFormatter().format(date)
}

fun getTodaysDate(): String {
    return getYYYMMddDateFormatter().format(Date())
}