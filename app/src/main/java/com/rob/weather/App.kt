package com.rob.weather

import android.app.Application
import com.rob.weather.di.CityListModule
import com.rob.weather.di.DaggerWeatherAppComponent
import com.rob.weather.di.WeatherAppComponent

class App: Application() {
    lateinit var component: WeatherAppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerWeatherAppComponent.builder().cityListModule(CityListModule(this)).build()
    }
}
