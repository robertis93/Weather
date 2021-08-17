package com.rob.weather.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rob.weather.Utils.returnTime
import com.rob.weather.databinding.TimeWeatherItemBinding
import com.rob.weather.model.ForecastResponse
import com.squareup.picasso.Picasso

class TimeAndTemperatureAdapter(private val allDaysWeatherList: List<ForecastResponse>) :
    RecyclerView.Adapter<TimeAndTemperatureAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding =
            TimeWeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val currentItem = allDaysWeatherList[position]

        holder.binding.timeTextView.text = returnTime(currentItem.date)
        holder.binding.temperatureTextView.text = Math.round(currentItem.main.temp).toString()+ "${"°"}"

        val iconCode = currentItem.weather.first().icon
        val iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png"
        Picasso.get().load(iconUrl).into(holder.binding.weatherIcon)
    }

    override fun getItemCount(): Int {
        return allDaysWeatherList.size
    }

    class WeatherViewHolder(val binding: TimeWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}