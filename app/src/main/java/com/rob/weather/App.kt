package com.rob.weather

import android.app.Application
import android.content.SharedPreferences
import com.rob.weather.di.*
import com.yandex.mapkit.MapKitFactory

class App : Application() {
    lateinit var component: WeatherAppComponent

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("040a037b-1287-4381-bb2c-5b20912a208e")
        component = DaggerWeatherAppComponent.builder()
            .remoteModule(RemoteModule(this))
            .applicationModule(ApplicationModule(this))
            .dataBaseModule(DataBaseModule())
            .build()
    }

    fun Application.getComponent(): WeatherAppComponent {
        if (this !is App) throw IllegalArgumentException("Для работы метода его надо вызывать у класса App.")
        return component
    }
}

