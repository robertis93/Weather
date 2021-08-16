package com.rob.weather.Utils

import java.text.SimpleDateFormat
import java.util.*

fun changeDateFormat(date: String): String {
    val oldDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
    val newDateFormat = SimpleDateFormat("dd.MM.yyyy EEE", Locale.getDefault())
    val changedDate = oldDateFormat.parse(date)
    return newDateFormat.format(changedDate)
}

