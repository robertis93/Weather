package com.rob.weather.viewmodel.repository

import com.rob.weather.viewmodel.Retrofit.RetrofitServices
import com.rob.weather.viewmodel.Retrofit.RetrofitServices.Companion.retrofitService

class Repository constructor(retrofitServices: RetrofitServices) {
    fun getAll(city : String, id : String) = retrofitService?.geWeatherForecastResponse(city, id)
}