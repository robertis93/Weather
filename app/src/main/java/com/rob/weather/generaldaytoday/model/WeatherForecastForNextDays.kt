package com.rob.weather.generaldaytoday.model

import android.os.Parcelable
import com.rob.weather.model.ForecastResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherForecastForNextDays(
    val date: String,
    var city: String,
    val weekDay: String,
    val minTemperatureForDay: String,
    val maxTemperatureForDay: String,
    val iconCode: String,
    val forecastResponseList: List<ForecastResponse>
) : Parcelable
