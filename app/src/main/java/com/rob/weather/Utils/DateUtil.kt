package com.rob.weather.Utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    private val fullDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
    private val shortDateFormat = SimpleDateFormat("dd MMMM, EEE", Locale.getDefault())
    val dayMonthFormat = SimpleDateFormat("dd MMMM,", Locale.getDefault())
    val weekDayFormat = SimpleDateFormat("EEE", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun changeDateFormat(date: String): String {
        val changedDate = fullDateFormat.parse(date)
        return shortDateFormat.format(changedDate)
    }

    fun returnDayOfWeek(date: String): String {
        val changedDate = dayMonthFormat.parse(date)
        return weekDayFormat.format(changedDate)
    }

    fun returnTime(date: String): String {
        val changedDate = fullDateFormat.parse(date)
        return timeFormat.format(changedDate)
    }

    fun dateStringToDayTimeStamp(date: String): String {
        @SuppressLint("SimpleDateFormat")
        val day: Date = fullDateFormat.parse(date)
        val dayTimestamp = day.time
        val timeFormat = SimpleDateFormat("HH:mm")
        val currentDate = Date(dayTimestamp)
        return timeFormat.format(currentDate)
    }
}
