package com.rob.weather

import android.app.Application
import com.rob.weather.citylist.database.WeatherDataBase
import com.rob.weather.di.DaggerWeatherAppComponent

import com.rob.weather.di.WeatherAppComponent

class App : Application() {
    lateinit var component: WeatherAppComponent


        // Using by lazy so the database and the repository are only created when they're needed
        // rather than when the application starts
        val database by lazy { WeatherDataBase.getDataBase(this) }
      //  val r by lazy { WordRepository(database.wordDao()) }

    override fun onCreate() {
        super.onCreate()
       component = DaggerWeatherAppComponent.create()
    }
}