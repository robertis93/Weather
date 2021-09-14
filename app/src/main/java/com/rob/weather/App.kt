package com.rob.weather

import android.app.Application
import androidx.room.Room
import com.rob.weather.citylist.database.WeatherDataBase
import com.rob.weather.di.DaggerWeatherAppComponent
import com.rob.weather.di.WeatherAppComponent
import javax.inject.Inject


class App @Inject constructor(): Application() {
    lateinit var component: WeatherAppComponent

    lateinit var instance: App

    lateinit var database: WeatherDataBase

    override fun onCreate() {
        super.onCreate()
        component = DaggerWeatherAppComponent.create()
        instance = this
        database = Room.databaseBuilder(this, WeatherDataBase::class.java, "database")
            .build()
    }

    @JvmName("getInstance1")
    fun getInstance(): App? {
        return instance
    }

    @JvmName("getDatabase1")
    fun getDatabase(): WeatherDataBase {
        return database
    }
}