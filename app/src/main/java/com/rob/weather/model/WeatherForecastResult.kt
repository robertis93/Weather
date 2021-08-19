package com.rob.weather.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherForecastResult(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ForecastResponse>,
    val message: Double
) : Parcelable

@Parcelize
data class City(
    @SerializedName("id") var id : Int,
    @SerializedName("name") var name : String,
    @SerializedName("coord") var coordinates : Сoordinates,
    @SerializedName("country") var country : String
) : Parcelable

@Parcelize
data class ForecastResponse (
    @SerializedName("dt") var dt : Int,
    @SerializedName("main") var main : Main,
    @SerializedName("weather") var weather : List<Weather>,
    @SerializedName("clouds") var clouds : Clouds,
    @SerializedName("wind") var wind : Wind,
    @SerializedName("rain") var rain : Rain,
    @SerializedName("sys") var sys : Sys,
    @SerializedName("dt_txt") var date : String
) : Parcelable

@Parcelize
data class Сoordinates (
    @SerializedName("lat") var latitude : Double,
    @SerializedName("lon") var longitude : Double
) : Parcelable

@Parcelize
data class Clouds (
    @SerializedName("all") var all : Int
) : Parcelable

@Parcelize
data class Main(
    val grnd_level: Double,
    val humidity: Int,
    val pressure: Double,
    val sea_level: Double,
    val temp: Double,
    val temp_kf: Double,
    val temp_max: Double,
    val temp_min: Double
) : Parcelable

@Parcelize
class Rain () : Parcelable

@Parcelize
data class Sys (
    @SerializedName("pod") var pod : String
) : Parcelable

@Parcelize
data class Weather (
    @SerializedName("id") var id : Int,
    @SerializedName("main") var main : String,
    @SerializedName("description") var description : String,
    @SerializedName("icon") var icon : String
) : Parcelable

@Parcelize
data class Wind (
    @SerializedName("speed") var speed : Double,
    @SerializedName("deg") var deg : Double,
    @SerializedName("gust") var gust : Double
) : Parcelable

@Parcelize
data class SortedByDateWeatherForecastResult(
    val date: String,
    val forecastResponseList: List<ForecastResponse>
) : Parcelable