package com.rob.weather.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    var city = "Тамбов"
    var id_key = "2e65127e909e178d0af311a81f39948c"

    @SuppressLint("ConstantLocale")
    val fullDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    @SuppressLint("ConstantLocale")
    val shortDateFormat = SimpleDateFormat("dd MMMM, EEE", Locale.getDefault())
    @SuppressLint("ConstantLocale")
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

}
