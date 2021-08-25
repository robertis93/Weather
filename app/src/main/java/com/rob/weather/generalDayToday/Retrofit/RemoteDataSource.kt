package com.rob.weather.generalDayToday.Retrofit

import com.rob.weather.model.WeatherForecastResult
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitServices {
    @GET("data/2.5/forecast?")
    fun geWeatherForecastResponse(
        @Query("q") q: String,
        @Query("appid") appid: String,
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "ru",
    ): Call<WeatherForecastResult>

    companion object {
        var retrofitService: RetrofitServices? = null
        fun getClient(baseUrl: String): RetrofitServices {
            if (retrofitService == null) {
               val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitServices::class.java)
            }
            return retrofitService!!
        }
    }
}