package com.rob.weather.citylist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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

//    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
//        //Log.v("", "Log position" + fromPosition + " " + toPosition);
//        //Log.v("", "Log position" + fromPosition + " " + toPosition);
//        if (fromPosition < cityList.size && toPosition < cityList.size) {
//            if (fromPosition < toPosition) {
//                for (i in fromPosition until toPosition) {
//                    Collections.swap(cityList, i, i + 1)
//                }
//            } else {
//                for (i in fromPosition downTo toPosition + 1) {
//                    Collections.swap(cityList, i, i - 1)
//                }
//            }
//            notifyItemMoved(fromPosition, toPosition)
//        }
//        return true
//    }

//    override fun onItemDismiss(position: Int) {
//      //  cityList.(position)
//        notifyItemRemoved(position)
//    }

}