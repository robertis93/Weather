package com.rob.weather.datasource.retrofit

import com.rob.weather.model.City
import com.rob.weather.model.WeatherForecastResult
import com.rob.weather.model.Сoordinates
import com.rob.weather.utils.Utils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

interface DataSource {
    suspend fun getWeatherForecastResponse(q: String): WeatherForecastResult
}

interface Some3{
    fun doSomething()
}

class Some3Impl @Inject constructor(): Some3{
    override fun doSomething() {
        TODO("Not yet implemented")
    }

}

class Some3Impl2 @Inject constructor(): Some3{
    override fun doSomething() {
        TODO("Not yet implemented")
    }

}

class Some2 @Inject constructor(val some3: Some3){

}

@Singleton
class RemoteDataSource @Inject constructor(val some2: Some2) : DataSource {
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


    companion object {
        const val BASE_URL = "https://api.openweathermap.org/"
    }
}

class FakeDataSource @Inject constructor(): DataSource{
    override suspend fun getWeatherForecastResponse(q: String): WeatherForecastResult{
        return WeatherForecastResult(
            City(1, "Moscow", Сoordinates(465.1,456.4), "Russia"),
            12, "sdf",
            listOf(),
            454.4
        )
    }
}