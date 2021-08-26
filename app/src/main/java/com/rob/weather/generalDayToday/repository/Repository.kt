package com.rob.weather.generalDayToday.repository

import com.rob.weather.generalDayToday.Retrofit.RemoteDataSource
import com.rob.weather.generalDayToday.Retrofit.RemoteDataSource.RetrofitServices.Companion.retrofitService

class Repository constructor(private val id : String, retrofitServices: RemoteDataSource.RetrofitServices) {
    suspend fun getAll(city : String) = retrofitService?.geWeatherForecastResponse(city, id)
}