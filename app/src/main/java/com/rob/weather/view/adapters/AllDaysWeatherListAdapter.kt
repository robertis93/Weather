package com.rob.weather.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rob.weather.Utils.changeDateFormat
import com.rob.weather.databinding.DayWeatherRowBinding
import com.rob.weather.model.ForecastResponse

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
      //  holder.binding.precipitationValueTextView.text = currentItem.rain.toString()
        holder.binding.pressureValueTextView.text = currentItem.main.pressure.toString()
        holder.binding.temperatureValueTextView.text = (currentItem.main.temp).toString() + "${" °C"}"
        holder.binding.windValueTextView.text = currentItem.wind.speed.toString()

        //  Picasso.get().load(allDaysWeatherList[position].pressure).into(holder.binding)

//        Picasso.get()
//            .load("https://openweathermap.org/img/w/")
//            .placeholder(allDaysWeatherList.)
//            .error(R.drawable.error)
//            .fit()
//            .into(imageView)

//        Picasso.with(context)
//            .load(url)
//            .placeholder(R.drawable.user_placeholder)
//            .error(R.drawable.user_placeholder_error)
//            .into(imageView);
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