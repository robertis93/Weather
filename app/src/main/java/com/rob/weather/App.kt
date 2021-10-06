package com.rob.weather

import android.app.Application
import com.rob.weather.di.CityListModule
import com.rob.weather.di.DaggerWeatherAppComponent
import com.rob.weather.di.WeatherAppComponent
import com.yandex.mapkit.MapKitFactory

class App: Application() {
    lateinit var component: WeatherAppComponent

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("040a037b-1287-4381-bb2c-5b20912a208e")
        component = DaggerWeatherAppComponent.builder().cityListModule(CityListModule(this)).build()
    }
}
