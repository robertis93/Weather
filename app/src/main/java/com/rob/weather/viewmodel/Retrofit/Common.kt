package com.rob.weather.viewmodel.Retrofit

object Common {
    private val BASE_URL = "https://api.openweathermap.org/"
    val retrofitService: RemoteDataSource.RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL)
            .create(RemoteDataSource.RetrofitServices::class.java)
}