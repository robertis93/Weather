package com.rob.weather

import android.app.Application
import androidx.room.Room
import com.rob.weather.citylist.database.WeatherDataBase
import com.rob.weather.di.DaggerWeatherAppComponent
import com.rob.weather.di.WeatherAppComponent


class App : Application() {
    lateinit var component: WeatherAppComponent
    var instance: App? = null

    lateinit var dataBase: WeatherDataBase

        // Using by lazy so the database and the repository are only created when they're needed
        // rather than when the application starts
       // var database by lazy { WeatherDataBase.getDataBase(this) }
  // val repository by lazy { WeatherRepository(database.cityDao()) }
   //     val repository by lazy { WeatherRepository(database.cityDao(), WeatherDataSource()}
        // Using by lazy so the database and the repository are only created when they're needed
        // rather than when the application starts
       // val database by lazy { WeatherDataBase.getDataBase(this) }
      //  val r by lazy { WordRepository(database.wordDao()) }

    override fun onCreate() {
        super.onCreate()
       component = DaggerWeatherAppComponent.create()
//        instance = this
//        dataBase = Room.databaseBuilder(this, WeatherDataBase::class.java, "database")
//            .build()

    }
}