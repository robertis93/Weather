package com.rob.weather.generaldaytoday.repository

import com.rob.weather.generaldaytoday.retrofit.RemoteDataSource
import com.rob.weather.generaldaytoday.retrofit.RemoteDataSource.RetrofitServices.Companion.retrofitService

class WeatherForecastRepository constructor(private val id : String, retrofitServices: RemoteDataSource.RetrofitServices) {
    suspend fun getWeatherForecast(city : String) = retrofitService?.geWeatherForecastResponse(city, id)
}