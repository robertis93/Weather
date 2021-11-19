package com.rob.weather.generaldaytoday.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.mikepenz.fastadapter.items.AbstractItem
import com.rob.weather.R
import com.rob.weather.databinding.TimeTemperatureItemBinding
import com.rob.weather.generaldaytoday.model.WeatherForecastForNextDays
import com.rob.weather.model.ForecastResponse
import com.rob.weather.utils.Utils
import com.squareup.picasso.Picasso

class WeatherForecastForNextDaysItem(var weatherForecastForNextDays: WeatherForecastForNextDays) :
    AbstractItem<WeatherForecastForNextDaysItem.ViewHolder>() {

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
        val temperatureDuringDayItemAdapter = ItemAdapter<BindingTemperatureItem>()
        val weatherForecastForNextDaysFastAdapter =
            FastAdapter.with(temperatureDuringDayItemAdapter)

        override fun bindView(item: WeatherForecastForNextDaysItem, payloads: List<Any>) {
            date.text = item.weatherForecastForNextDays.date
            dayOfWeek.text = item.weatherForecastForNextDays.weekDay
            maxTemperature.text = item.weatherForecastForNextDays.maxTemperatureForDay
            minTemperature.text = item.weatherForecastForNextDays.minTemperatureForDay
            val iconUrl = "https://openweathermap.org/img/w/" +
                    item.weatherForecastForNextDays.iconCode + ".png"
            Picasso.get().load(iconUrl).into(iconWeather)
            recyclerView.adapter = weatherForecastForNextDaysFastAdapter
            val temperatureWithTineList = item.weatherForecastForNextDays.forecastResponseList
            for (k in 1 until temperatureWithTineList.size) {
                temperatureDuringDayItemAdapter.add(BindingTemperatureItem(temperatureWithTineList[k]))
            }
        }

        override fun unbindView(item: WeatherForecastForNextDaysItem) {
            date.text = null
            dayOfWeek.text = null
            maxTemperature.text = null
            minTemperature.text = null
        }
    }
}

class BindingTemperatureItem(val forecastResponse: ForecastResponse) :
    AbstractBindingItem<TimeTemperatureItemBinding>() {
    var name: String? = null

    override val type: Int
        get() = R.id.temperature_during_day_recyclerview

    override fun bindView(binding: TimeTemperatureItemBinding, payloads: List<Any>) {
        binding.timeTextView.text = (forecastResponse.date).returnTime()
        binding.temperatureTextView.text =
            forecastResponse.main.temp_max.toInt().toString() + "${"°"}"
        val iconCode = forecastResponse.weather.first().icon
        val iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png"
        Picasso.get().load(iconUrl).into(binding.weatherIcon)
    }

    override fun unbindView(binding: TimeTemperatureItemBinding) {
        binding.weatherIcon.setImageDrawable(null)
        binding.temperatureTextView.text = null
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): TimeTemperatureItemBinding {
        return TimeTemperatureItemBinding.inflate(inflater, parent, false)
    }
}

fun String.returnTime(): String {
    val changedDate = Utils.fullDateFormat.parse(this)
    return Utils.timeFormat.format(changedDate)
}

