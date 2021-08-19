package com.rob.weather.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rob.weather.Utils.returnDayOfWeek
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
        //TODO : неправильно отображается min, max температура, отображается в заданный час, не за день
        holder.binding.dayTextView.text = currentItem.date.substringBefore(",") + ","
        holder.binding.weekDayTextView.text = currentItem.date.substringAfter(",")
        holder.binding.minTemperatureTextView.text =
            Math.round(currentItem.forecastResponseList.last().main.temp_min).toString() + "${"°"}"
        holder.binding.maxTemperatureTextView.text =
            Math.round(currentItem.forecastResponseList.first().main.temp_max).toString() + "${"°"}"
        val iconCode = currentItem.forecastResponseList.first().weather.first().icon
        val iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png"
        Picasso.get().load(iconUrl).into(holder.binding.imageView)

//        holder.itemView.setOnClickListener {
//            val action =
//                AllDaysWeatherInformationFragmentDirections.actionWeatherInformationByDayFragmentToChooseDayFragment(
//                    currentItem
//                )
//            holder.binding.root.findNavController().navigate(action)
//        }

        val timeAndTemperatureAdapter =
            TimeAndTemperatureAdapter(currentItem.forecastResponseList)
        val recyclerView = holder.binding.recyclerView
        recyclerView.adapter = timeAndTemperatureAdapter
        recyclerView.layoutManager = LinearLayoutManager(
            holder.binding.root.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    override fun getItemCount(): Int {
        return allDaysWeatherList.size
    }

    fun setData(forecastResponse: List<SortedByDateWeatherForecastResult>) {
        this.allDaysWeatherList = forecastResponse
        notifyDataSetChanged()
    }

    class WeatherViewHolder(val binding: DayWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}