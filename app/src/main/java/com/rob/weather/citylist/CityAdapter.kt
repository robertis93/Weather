package com.rob.weather.citylist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rob.weather.citylist.fragment.CityListFragmentDirections
import com.rob.weather.citylist.model.WeatherCity
import com.rob.weather.utils.BASE_URL_IMAGE
import com.squareup.picasso.Picasso

//class CityAdapter : RecyclerView.Adapter<CityAdapter.WeatherViewHolder>() {
//    private var cityList = emptyList<WeatherCity>()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
////        val binding =
////            CityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return WeatherViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
//        val currentItem = cityList[position]
//        holder.bind(currentItem, cityList)
//    }
//
//    override fun getItemCount(): Int {
//        return cityList.size
//    }
//
//    fun setData(listCity: List<WeatherCity>) {
//        cityList = listCity
//        notifyDataSetChanged()
//    }
//
//    class WeatherViewHolder(private val binding: CityItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        @SuppressLint("SetTextI18n")
//        fun bind(item: WeatherCity, cityList:List<WeatherCity>) {
//            binding.cityNameTextview.text = item.name
//            binding.minTemperatureText.text = item.temperatureMin.toString() + "°"
//            binding.maxTemperatureText.text = item.temperatureMax.toString() + "°"
//            val iconCode = item.icon
//            val iconUrl = BASE_URL_IMAGE + iconCode + ".png"
//            Picasso.get().load(iconUrl).into(binding.weatherIcon)
//            binding.rowLayout.setOnClickListener {
//                val action =
//                    CityListFragmentDirections.actionCityListFragmentToMapsFragment(item,
//                        cityList.toTypedArray()
//                    )
//                binding.root.findNavController().navigate(action)
//            }
//        }
//    }
//}