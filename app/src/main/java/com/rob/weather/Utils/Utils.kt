package com.rob.weather.Utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

fun changeDateFormat(date: String): String {
    val oldDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
    val newDateFormat = SimpleDateFormat("dd MMMM, EEE", Locale.getDefault())
    val changedDate = oldDateFormat.parse(date)
    return newDateFormat.format(changedDate)
}

fun returnDayOfWeek(date: String): String {
    val oldDateFormat = SimpleDateFormat("dd MMMM,", Locale.getDefault())
    val newDateFormat = SimpleDateFormat("EEE", Locale.getDefault())
    val changedDate = oldDateFormat.parse(date)
    return newDateFormat.format(changedDate)
}

fun returnTime(date: String): String {
    val oldDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
    val newDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val changedDate = oldDateFormat.parse(date)
    return newDateFormat.format(changedDate)
}

fun dateStringToDayTimeStamp(date: String): String {
    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    val day: Date = dateFormat.parse(date)
    val dayTimestamp = day.time
    val timeFormat = SimpleDateFormat("HH:mm")
    val currentDate = Date(dayTimestamp)
    return timeFormat.format(currentDate)
}


