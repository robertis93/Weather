package com.rob.weather.datasource.retrofit

import com.rob.weather.model.WeatherForecastResult
import com.rob.weather.utils.Utils.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class WeatherDataFromRemoteSource @Inject constructor(private val retrofitService: RetrofitServices) {

    suspend fun getWeatherForecastResponse(q: String): WeatherForecastResult {
        return retrofitService.getWeatherForecastResponse(q)
    }
}
