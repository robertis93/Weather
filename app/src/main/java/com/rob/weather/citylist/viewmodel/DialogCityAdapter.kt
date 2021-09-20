package com.rob.weather.citylist.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rob.weather.databinding.CityChipBinding

class DialogCityAdapter(private var onClickAlarmListener: OnAlarmClickListener):
    RecyclerView.Adapter<DialogCityAdapter.WeatherViewHolder>() {
    private var cityList = emptyList<String>()

   // lateinit var onClickAlarmListener: OnAlarmClickListener
   interface OnAlarmClickListener {
       fun onDeleteAlarmClick(alarm: String, position: Int)
   }
//    interface OnAlarmClickListener {
//        fun onDeleteAlarmClick(nameCity : String, position: Int)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding =
            CityChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val currentItem = cityList[position]
       holder.binding.chip.text = currentItem
       // holder.bind(currentItem)
        //holder.itemView.setOnClickListener {    onClickAlarmListener?.onDeleteAlarmClick(currentItem, position) }
//        holder.itemView.setOnClickListener {
//            //we can then create an intent here and start a new activity
//            //with our data
//        }
        holder.binding.chip.setOnClickListener {
            onClickAlarmListener.onDeleteAlarmClick(currentItem, position)
        }
       //holder.bind(cityList.get(position), onClickAlarmListener)
//        holder.binding.chip.setOnClickListener {
//            onClickAlarmListener?.onDeleteAlarmClick(currentItem, position)
//            notifyDataSetChanged()
//        }
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    fun setData(listCity: List<String>) {
        this.cityList = listCity
        notifyDataSetChanged()
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
