package com.rob.weather.datasource.retrofit

import com.rob.weather.utils.URL_TO_GET_IP
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface RetrofitServicesIP {

    @GET("json/?")
    fun receiveCityByIp(
        @Url url: String = URL_TO_GET_IP,
        @Query("lang") language: String = "ru",
    ): RetrofitServicesIP
}