package com.rob.weather.viewmodel.repository.Retrofit

object Common {
    private val BASE_URL = "https://openweathermap.org/api"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}