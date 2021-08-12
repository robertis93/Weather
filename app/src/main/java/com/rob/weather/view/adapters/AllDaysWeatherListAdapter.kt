package com.rob.weather.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rob.weather.databinding.AllDaysWeatherItemBinding
import com.rob.weather.databinding.OneDayWeatherItemBinding
import com.rob.weather.model.WeatherAllDaysEntity
import com.rob.weather.model.WeatherOneDayEntity

class AllDaysWeatherListAdapter : RecyclerView.Adapter<AllDaysWeatherListAdapter.WeatherViewHolder>() {

    private var allDaysWeatherList = emptyList<WeatherAllDaysEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding =
            AllDaysWeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val currentItem = allDaysWeatherList[position]

        holder.binding.dayTextView = TODO()
        holder.binding.precipitationValueTextView = TODO()
        holder.binding.pressureValueTextView = TODO()
        holder.binding.temperatureValueTextView = TODO()
        holder.binding.windValueTextView = TODO()
    }

    override fun getItemCount(): Int {
        return allDaysWeatherList.size
    }

    class WeatherViewHolder(val binding: AllDaysWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}