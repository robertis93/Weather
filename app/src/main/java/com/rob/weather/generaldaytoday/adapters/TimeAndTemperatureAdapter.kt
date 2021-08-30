package com.rob.weather.generaldaytoday.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rob.weather.databinding.TimeTemperatureItemBinding
import com.rob.weather.model.ForecastResponse
import com.rob.weather.utils.Utils
import com.squareup.picasso.Picasso

class TimeAndTemperatureAdapter(private val allDaysWeatherList: List<ForecastResponse>) :
    RecyclerView.Adapter<TimeAndTemperatureAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding =
            TimeTemperatureItemBinding.inflate(
                LayoutInflater.from(parent.context), parent,
                false
            )
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val currentItem = allDaysWeatherList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return allDaysWeatherList.size
    }

    class WeatherViewHolder(private val binding: TimeTemperatureItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ForecastResponse) {
            binding.timeTextView.text = (item.date).returnTime()
            binding.temperatureTextView.text = item.main.temp.toInt().toString() + "${"°"}"
            val iconCode = item.weather.first().icon
            val iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png"
            Picasso.get().load(iconUrl).into(binding.weatherIcon)
        }
    }
}

fun String.returnTime(): String {
    val changedDate = Utils.fullDateFormat.parse(this)
    return Utils.timeFormat.format(changedDate)
}