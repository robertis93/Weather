package com.rob.weather.generaldaytoday.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.rob.weather.databinding.DayWeatherItemBinding
import com.rob.weather.generaldaytoday.fragment.GeneralDayTodayFragmentDirections
import com.rob.weather.generaldaytoday.model.WeatherForecastForNextDays
import com.squareup.picasso.Picasso

class GeneralDayTodayAdapter :
    RecyclerView.Adapter<GeneralDayTodayAdapter.WeatherViewHolder>() {
    private var allDaysWeatherList = emptyList<WeatherForecastForNextDays>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding =
            DayWeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val currentItem = allDaysWeatherList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return allDaysWeatherList.size
    }

    fun setData(forecastResponse: List<WeatherForecastForNextDays>) {
        allDaysWeatherList = forecastResponse
        notifyDataSetChanged()
    }

    class WeatherViewHolder(private val binding: DayWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WeatherForecastForNextDays) {
            binding.dayTextView.text = item.date
            binding.weekDayTextView.text = item.weekDay
            val minTemperatureForDay = item.minTemperatureForDay
            binding.minTemperatureTextView.text = minTemperatureForDay
            val maxTemperatureForDay = item.maxTemperatureForDay
            binding.maxTemperatureTextView.text = maxTemperatureForDay
            val iconCode = item.iconCode
            val iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png"
            Picasso.get().load(iconUrl).into(binding.imageView)
            val timeAndTemperatureAdapter =
                TimeAndTemperatureAdapter(item.forecastResponseList)
            val recyclerView = binding.recyclerView
            recyclerView.adapter = timeAndTemperatureAdapter
            binding.rowLayout.setOnClickListener {
                val action =
                    GeneralDayTodayFragmentDirections
                        .actionWeatherInformationByDayFragmentToChooseDayFragment3(item)
                binding.root.findNavController().navigate(action)
            }
        }
    }
}