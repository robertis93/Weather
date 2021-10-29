package com.rob.weather.generaldaytoday.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.rob.weather.model.ForecastResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherForecastForNextDays(
    val date: String,
    var city: String,
    val weekDay: String,
    val minTemperatureForDay: String,
    val maxTemperatureForDay: String,
    val iconCode: String,
    val forecastResponseList: List<ForecastResponse>
) : Parcelable

@Parcelize
data class ResponseByIp(
    @SerializedName("status") var status: String,
    @SerializedName("country") var country: String,
    @SerializedName("countryCode") var countryCode: String,
    @SerializedName("region") var region: String,
    @SerializedName("regionName") var regionName: String,
    @SerializedName("city") var city: String,
    @SerializedName("zip") var zip: String,
    @SerializedName("lat") var lat: Double,
    @SerializedName("lon") var lon: Double,
    @SerializedName("timezone") var timezone: String,
    @SerializedName("isp") var isp: String,
    @SerializedName("org") var org: String,
    @SerializedName("as") var operator: String,
    @SerializedName("query") var query: String
) : Parcelable
