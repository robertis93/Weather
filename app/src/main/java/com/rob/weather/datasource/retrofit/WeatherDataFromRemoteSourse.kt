package com.rob.weather.datasource.retrofit

import com.rob.weather.model.WeatherForecastResult
import com.rob.weather.utils.Utils
import com.rob.weather.utils.Utils.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

interface WeatherDataSource {
    suspend fun getWeatherForecastResponse(q: String): WeatherForecastResult
}

class WeatherDataFromRemoteSource @Inject constructor() : WeatherDataSource {


    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val retrofitService: RetrofitServices by lazy {
        retrofit.create(RetrofitServices::class.java)
    }

    override suspend fun getWeatherForecastResponse(q: String): WeatherForecastResult{
        return retrofitService.getWeatherForecastResponse(q)
    }
}
