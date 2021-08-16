package com.rob.weather.viewmodel.Retrofit

import com.rob.weather.model.WeatherForecastResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

class RemoteDataSource {
    interface RetrofitServices {
        @GET("data/2.5/forecast?")
        fun geWeatherForecastResponse(
            @Query("q") q: String,
            @Query("appid") appid: String,
            @Query("units") units: String = "metric",
            @Query("lang") language: String = "ru",
        ): Call<WeatherForecastResult>
    }
}