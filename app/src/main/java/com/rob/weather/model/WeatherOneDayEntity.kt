package com.rob.weather.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_one_day_table")
data class WeatherOneDayEntity(
    @PrimaryKey
    var id: String,
    val city : String,
    val date : String,
    val temperature : Int
   // TODO add image
)