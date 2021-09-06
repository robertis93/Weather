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

interface DataSource {
    suspend fun getWeatherForecastResponse(q: String): WeatherForecastResult
}

@Singleton
class RemoteDataSource @Inject constructor() : DataSource {
    private interface RetrofitServices {
        @GET("data/2.5/forecast?")
        suspend fun getWeatherForecastResponse(
            @Query("q") q: String,
            @Query("appid") appid: String = Utils.id_key,
            @Query("units") units: String = "metric",
            @Query("lang") language: String = "ru",
        ): WeatherForecastResult
    }

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
