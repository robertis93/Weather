package com.rob.weather.datasource.retrofit

import com.rob.weather.model.WeatherForecastResult
import com.rob.weather.utils.Utils
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitServices {
    @GET("data/2.5/forecast?")
    suspend fun getWeatherForecastResponse(
        @Query("q") q: String,
        @Query("appid") appid: String = Utils.id_key,
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "ru",
    ): WeatherForecastResult
}