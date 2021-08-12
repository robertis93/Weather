package com.rob.weather.viewmodel.repository.Retrofit

import android.graphics.Movie
import com.rob.weather.model.WeatherOneDayEntity
import retrofit2.Call
import retrofit2.http.*

interface RetrofitServices {
    @GET("weather")
    fun getMovieList(): Call<MutableList<WeatherOneDayEntity>>
}