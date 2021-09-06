package com.rob.weather.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    var city = "Тамбов"
    var id_key = "2e65127e909e178d0af311a81f39948c"
    const val BASE_URL = "https://api.openweathermap.org/"

    @SuppressLint("ConstantLocale")
    val fullDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    @SuppressLint("ConstantLocale")
    val shortDateFormat = SimpleDateFormat("dd MMMM, EEE", Locale.getDefault())
    @SuppressLint("ConstantLocale", "SimpleDateFormat")
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val hourFormat = SimpleDateFormat("HH", Locale.getDefault())

}
