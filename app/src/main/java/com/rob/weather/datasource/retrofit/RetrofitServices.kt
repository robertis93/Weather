package com.rob.weather.datasource.retrofit

import com.rob.weather.generaldaytoday.model.ResponseByIp
import com.rob.weather.model.WeatherForecastResult
import com.rob.weather.utils.id_key
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface RetrofitServices {
    @GET("data/2.5/forecast?")
    suspend fun getWeatherForecastResponse(
        @Query("q") q: String,
        @Query("appid") appid: String = id_key,
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "ru",
    ): WeatherForecastResult

    @GET("data/2.5/forecast?")
    suspend fun getWeatherInformationByLatitudeAndLongitude(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String = id_key,
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "ru",
    ): WeatherForecastResult

    @GET
    suspend fun getIP(
        @Url url : String,
        @Query("lang") language: String = "ru",
    ): ResponseByIp
}