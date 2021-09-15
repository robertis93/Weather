package com.rob.weather.citylist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rob.weather.citylist.model.City
import com.rob.weather.citylist.model.WeatherCity
import com.rob.weather.databinding.CityItemBinding
import com.squareup.picasso.Picasso

class CityAdapter: RecyclerView.Adapter<CityAdapter.WeatherViewHolder>() {
    private var cityList = emptyList<WeatherCity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding =
            CityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val currentItem = cityList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    fun setData(listCity: List<WeatherCity>) {
        this.cityList = listCity
        notifyDataSetChanged()
    }

    class WeatherViewHolder(private val binding:  CityItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: WeatherCity) {
            binding.dayTextView.text = item.name
            binding.minTemperatureTextView.text = item.temperatureMin.toString() + "°"
            binding.maxTemperatureTextView.text = item.temperatureMax.toString() + "°"
            val iconCode = item.icon
            val iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png"
            Picasso.get().load(iconUrl).into(binding.imageView)
        }
    }
}