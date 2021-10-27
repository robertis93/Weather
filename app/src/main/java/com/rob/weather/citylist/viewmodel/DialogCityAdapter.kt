package com.rob.weather.citylist.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rob.weather.databinding.CityChipBinding

class DialogCityAdapter(private var onClickCityListener: OnCityClickListener):
    RecyclerView.Adapter<DialogCityAdapter.WeatherViewHolder>() {
    private var cityList = emptyList<String>()
   interface OnCityClickListener {
       fun onCityClick(cityName: String, position: Int)
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding =
            CityChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val currentItem = cityList[position]
       holder.binding.chip.text = currentItem

        holder.binding.chip.setOnClickListener {
            onClickCityListener.onCityClick(currentItem, position)
        }
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    fun setData(listCity: List<String>) {
        this.cityList = listCity
        this.notifyDataSetChanged()
    }

    class WeatherViewHolder(val binding:  CityChipBinding) :
        RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: String, onClickAlarmListener: OnAlarmClickListener) {
//            binding.chip.text = item
//            itemView.setOnClickListener {
//                onClickAlarmListener.onDeleteAlarmClick(item, position)
//            }
//        }
    }
}
//interface OnItemClickListener{
//    fun onItemClicked(user: String)
//}
