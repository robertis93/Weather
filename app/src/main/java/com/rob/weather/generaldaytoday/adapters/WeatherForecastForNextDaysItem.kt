package com.rob.weather.generaldaytoday.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.rob.weather.R
import com.rob.weather.generaldaytoday.model.WeatherForecastForNextDays
import com.squareup.picasso.Picasso

class WeatherForecastForNextDaysItem(var weatherForecastForNextDays: WeatherForecastForNextDays) :
    AbstractItem<WeatherForecastForNextDaysItem.ViewHolder>() {
    private val recyclerView: RecyclerView? = null
    override val type: Int
        get() = R.id.weather_forecast_recyclerView

    override val layoutRes: Int
        get() = R.layout.day_weather_item

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<WeatherForecastForNextDaysItem>(view) {
        var date: TextView = view.findViewById(R.id.dayTextView)
        var dayOfWeek: TextView = view.findViewById(R.id.weekDayTextView)
        val iconWeather: ImageView = view.findViewById(R.id.imageView)
        var maxTemperature: TextView = view.findViewById(R.id.maxTemperatureTextView)
        var minTemperature: TextView = view.findViewById(R.id.minTemperatureTextView)
        val recyclerView: RecyclerView = view.findViewById(R.id.temperature_during_day_recyclerview)

        //        var tempetatureList: TextView = view.findViewById(R.id.temperature_recyclerView)
        override fun bindView(item: WeatherForecastForNextDaysItem, payloads: List<Any>) {
            date.text = item.weatherForecastForNextDays.date
            dayOfWeek.text = item.weatherForecastForNextDays.weekDay
            maxTemperature.text = item.weatherForecastForNextDays.maxTemperatureForDay
            minTemperature.text = item.weatherForecastForNextDays.minTemperatureForDay

            val iconUrl = "https://openweathermap.org/img/w/" +
                    item.weatherForecastForNextDays.iconCode + ".png"
            Picasso.get().load(iconUrl).into(iconWeather)

            val timeAndTemperatureAdapter =
                TimeAndTemperatureAdapter(item.weatherForecastForNextDays.forecastResponseList)
            recyclerView.adapter = timeAndTemperatureAdapter
        }

        override fun unbindView(item: WeatherForecastForNextDaysItem) {
            date.text = null
            dayOfWeek.text = null
            maxTemperature.text = null
            minTemperature.text = null
        }
    }
}

