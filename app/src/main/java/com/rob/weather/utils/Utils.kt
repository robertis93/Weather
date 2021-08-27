package com.rob.weather.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    var city = "Тамбов"
    var id_key = "2e65127e909e178d0af311a81f39948c"
    @SuppressLint("ConstantLocale")
    private val fullDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    @SuppressLint("ConstantLocale")
    private val shortDateFormat = SimpleDateFormat("dd MMMM, EEE", Locale.getDefault())
    @SuppressLint("ConstantLocale")
    val dayMonthFormat = SimpleDateFormat("dd MMMM,", Locale.getDefault())

    @SuppressLint("ConstantLocale")
    val weekDayFormat = SimpleDateFormat("EEE", Locale.getDefault())

    @SuppressLint("SimpleDateFormat", "ConstantLocale")
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun String.changeDateFormat(): String {
        val changedDate = fullDateFormat.parse(this)
        return shortDateFormat.format(changedDate)
    }

    fun String.returnTime(): String {
        val changedDate = fullDateFormat.parse(this)
        return timeFormat.format(changedDate)
    }
}
