package com.rob.weather.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm_table")
data class WeatherOneDayEntity(
    @PrimaryKey
    var id: String,
   // TODO
)