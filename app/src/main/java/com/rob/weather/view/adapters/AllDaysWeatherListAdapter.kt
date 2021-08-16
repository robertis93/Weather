package com.rob.weather.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rob.weather.Utils.changeDateFormat
import com.rob.weather.databinding.DayWeatherRowBinding
import com.rob.weather.model.ForecastResponse
import com.squareup.picasso.Picasso

class AllDaysWeatherListAdapter :
    RecyclerView.Adapter<AllDaysWeatherListAdapter.WeatherViewHolder>() {

    private var allDaysWeatherList = emptyList<ForecastResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding =
            DayWeatherRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val currentItem = allDaysWeatherList[position]

        holder.binding.dayTextView.text = changeDateFormat(currentItem.date)
        holder.binding.precipitation.text = currentItem.weather.first().description
        holder.binding.precipitationValueTextView.text = currentItem.main.temp_kf.toString()
        holder.binding.pressureValueTextView.text = currentItem.main.pressure.toString()
        holder.binding.temperatureValueTextView.text =
            (Math.round(currentItem.main.temp)).toString() + "${" °C"}"
        holder.binding.windValueTextView.text = currentItem.wind.speed.toString()
        val iconCode = currentItem.weather.first().icon
        val iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png"
        Picasso.get().load(iconUrl).into(holder.binding.weatherIcon);
    }

    override fun getItemCount(): Int {
        return allDaysWeatherList.size
    }

    fun setData(forecastResponse: List<ForecastResponse>) {
        this.allDaysWeatherList = forecastResponse
        notifyDataSetChanged()
    }

    class WeatherViewHolder(val binding: DayWeatherRowBinding) :
        RecyclerView.ViewHolder(binding.root)
}