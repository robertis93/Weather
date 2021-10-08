package com.rob.weather.citylist.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "city_table")
data class City(
    @PrimaryKey()
    val name: String
)

@Parcelize
data class WeatherCity(
    @PrimaryKey
    val name: String,
    val temperatureMax: Int,
    val temperatureMin: Int,
    var icon: String,
    var latitude: Double,
    var longitude: Double
) : Parcelable
