package com.mbmc.fiinfo.util

import java.text.DateFormat
import java.util.*

// TODO: am/pm lowercase
fun getDate(timestamp: Long, timeZone: String): String {
    val dateFormat = DateFormat.getDateTimeInstance(
        DateFormat.SHORT,
        DateFormat.MEDIUM, Locale.getDefault()
    )
    dateFormat.timeZone = TimeZone.getTimeZone(timeZone)
    return dateFormat.format(timestamp)
}