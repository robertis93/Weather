package com.rob.weather.citylist.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "city_table")
data class City (
    @PrimaryKey()
    val name: String
)

data class WeatherCity (
    @PrimaryKey
    val name: String,
    val temperatureMax: Int,
    val temperatureMin: Int,
    var icon: String
)
