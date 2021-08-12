package com.rob.weather.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rob.weather.databinding.OneDayWeatherItemBinding
import com.rob.weather.model.WeatherOneDayEntity

class WeatherListAdapter : RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>() {

    private var weatherOneDayList = emptyList<WeatherOneDayEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding =
            OneDayWeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val currentItem = weatherOneDayList[position]

        holder.binding.dayTextView = TODO()
        holder.binding.temperature = TODO()
        holder.binding.weatherIcon = TODO()
    }

    override fun getItemCount(): Int {
        return weatherOneDayList.size
    }

    class WeatherViewHolder(val binding: OneDayWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}