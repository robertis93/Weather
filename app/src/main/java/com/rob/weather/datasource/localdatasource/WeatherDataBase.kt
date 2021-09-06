package com.rob.weather.datasource.localdatasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rob.weather.model.WeatherForecastResult

@Database(entities = [WeatherForecastResult::class], version = 1)
abstract class WeatherDataBase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}