package com

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.rob.weather.R
import com.squareup.picasso.Picasso

class DisplayShortInfoWeather(context: Context) : LinearLayout(context) {

    private var iconCityWeather: ImageView
    private var minTemperatureInCity: TextView
    private var maxTemperatureInCity: TextView

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.map_custom_view, this, true)
        iconCityWeather = view.findViewById(R.id.weather_map_icon)
        minTemperatureInCity = view.findViewById(R.id.min_temperature_text)
        maxTemperatureInCity = view.findViewById(R.id.max_temperature_text)
    }

    fun setIconWeather(icon: String) {
        val iconUrl = "https://openweathermap.org/img/w/" + icon + ".png"
        Picasso.get().load(iconUrl).into(iconCityWeather)
    }

    fun setTemperature(min: Int, max: Int) {
        minTemperatureInCity.text = min.toString() + "°"
        maxTemperatureInCity.text = max.toString() + "°"
    }
}

