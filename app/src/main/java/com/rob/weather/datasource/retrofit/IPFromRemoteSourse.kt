package com.rob.weather.datasource.retrofit

import com.google.android.play.core.internal.q
import com.rob.weather.model.WeatherForecastResult
import javax.inject.Inject

class IPFromRemoteSource @Inject constructor(private val retrofitService: RetrofitServicesIP) {

    suspend fun getWeatherForecastResponse():
            RetrofitServicesIP {
        return retrofitService.receiveCityByIp()
    }
}
