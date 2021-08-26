package com.rob.weather.generalDayToday.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rob.weather.databinding.DayWeatherItemBinding
import com.rob.weather.model.SortedByDateWeatherForecastResult
import com.squareup.picasso.Picasso

class GeneralDayTodayAdapter :
    RecyclerView.Adapter<GeneralDayTodayAdapter.WeatherViewHolder>() {

    private var allDaysWeatherList = emptyList<SortedByDateWeatherForecastResult>()

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

    fun setData(forecastResponse: List<SortedByDateWeatherForecastResult>) {
        this.allDaysWeatherList = forecastResponse
        notifyDataSetChanged()
    }

    class WeatherViewHolder(private val binding: DayWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SortedByDateWeatherForecastResult) {
            binding.dayTextView.text = item.date.substringBefore(",") + ","
            binding.dayTextView.text = item.date.substringBefore(",") + ","
            binding.weekDayTextView.text = item.date.substringAfter(",")
            val minTemperatureForDay = item.forecastResponseList.stream().min { o1, o2 ->
                compareValues((o1.main.temp_min), (o2.main.temp_min))
            }.map { it.main.temp_min }.get().toInt().toString()
            binding.minTemperatureTextView.text = minTemperatureForDay
            val maxTemperatureForDay = item.forecastResponseList.stream().max { o1, o2 ->
                compareValues((o1.main.temp_min), (o2.main.temp_min))
            }.map { it.main.temp_max }.get().toInt().toString()
            binding.maxTemperatureTextView.text = maxTemperatureForDay
            val iconCode = item.forecastResponseList.first().weather.first().icon
            val iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png"
            Picasso.get().load(iconUrl).into(binding.imageView)
            val timeAndTemperatureAdapter =
                TimeAndTemperatureAdapter(item.forecastResponseList)
            val recyclerView = binding.recyclerView
            recyclerView.adapter = timeAndTemperatureAdapter
        }
    }
}