package com.rob.weather.generalDayToday.repository

import com.rob.weather.generalDayToday.Retrofit.RetrofitServices
import com.rob.weather.generalDayToday.Retrofit.RetrofitServices.Companion.retrofitService

class Repository constructor(retrofitServices: RetrofitServices) {
    fun getAll(city : String, id : String) = retrofitService?.geWeatherForecastResponse(city, id)
}