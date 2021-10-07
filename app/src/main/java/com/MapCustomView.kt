package com

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.rob.weather.R
import org.w3c.dom.Text
import java.util.jar.Attributes

class MapCustomView (context: Context) : LinearLayout(context) {

   var iconWeather : ImageView
     var temperatureWeather : TextView
    init {
val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.map_custom_view, this, true)
        iconWeather = view.findViewById(R.id.weather_icon_map)
        temperatureWeather  = view.findViewById(R.id.min_temperature_text)
    }
}

