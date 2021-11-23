package com.rob.weather.citylist

import com.mikepenz.fastadapter.diff.DiffCallback
import com.rob.weather.citylist.fragment.CityWeatherItem

class CityWeatherItemDiffCallback : DiffCallback<CityWeatherItem> {

    override fun areItemsTheSame(oldItem: CityWeatherItem, newItem: CityWeatherItem): Boolean {
        return oldItem.weatherCity == newItem.weatherCity
    }

    override fun areContentsTheSame(oldItem: CityWeatherItem, newItem: CityWeatherItem): Boolean {
        return oldItem.weatherCity == newItem.weatherCity
    }

    override fun getChangePayload(
        oldItem: CityWeatherItem,
        oldItemPosition: Int,
        newItem: CityWeatherItem,
        newItemPosition: Int
    ): Any? {
        return null
    }
}