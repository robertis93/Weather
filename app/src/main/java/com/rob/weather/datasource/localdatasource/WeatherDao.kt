package com.rob.weather.datasource.localdatasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.rob.weather.model.WeatherForecastResult

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weatherforecastresult")
    fun getWeather(): List<WeatherForecastResult>

    @Insert
    fun insert(weatherForecastResult: WeatherForecastResult)

    @Delete
    fun remove(weatherForecastResult: WeatherForecastResult)
}