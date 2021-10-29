package com.rob.weather.datasource.retrofit

import com.rob.weather.generaldaytoday.model.ResponseByIp
import com.rob.weather.model.WeatherForecastResult
import com.rob.weather.utils.URL_TO_GET_IP
import javax.inject.Inject

class WeatherDataFromRemoteSource @Inject constructor(private val retrofitService: RetrofitServices) {

    suspend fun getWeatherForecastResponse(q: String): WeatherForecastResult {
        return retrofitService.getWeatherForecastResponse(q)
    }

    suspend fun getWeatherInformationByLatitudeAndLongitude(
        lat: Double,
        lon: Double
    ): WeatherForecastResult {
        return retrofitService.getWeatherInformationByLatitudeAndLongitude(
            lat.toString(),
            lon.toString()
        )
    }

    suspend fun getIP(): ResponseByIp {
        return retrofitService.getIP(URL_TO_GET_IP)
    }

}
