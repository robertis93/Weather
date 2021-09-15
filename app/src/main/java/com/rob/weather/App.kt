package com.rob.weather

import android.app.Application
import androidx.room.Room
import com.rob.weather.citylist.database.WeatherDataBase
import com.rob.weather.di.DaggerWeatherAppComponent
import com.rob.weather.di.WeatherAppComponent
import javax.inject.Inject


class App @Inject constructor(): Application() {
    lateinit var component: WeatherAppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerWeatherAppComponent.create()
    }
}