package com.rob.weather.citylist

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.mikepenz.fastadapter.listeners.TouchEventHook
import com.rob.weather.R
import com.rob.weather.citylist.model.WeatherCity
import com.rob.weather.databinding.CityWeatherItemBinding
import com.rob.weather.utils.BASE_URL_IMAGE
import com.squareup.picasso.Picasso

class BindingWeatherInCityItem(var weatherCity: WeatherCity) :
    AbstractBindingItem<CityWeatherItemBinding>() {

    override val type: Int
        get() = R.id.city_list_recyclerview

    override fun bindView(binding: CityWeatherItemBinding, payloads: List<Any>) {
        binding.cityNameTextview.text = weatherCity.name
        binding.minTemperatureText.text = weatherCity.temperatureMin.toString()
        binding.maxTemperatureText.text = weatherCity.temperatureMax.toString()
        val iconCode = weatherCity.icon
        val iconUrl = BASE_URL_IMAGE + iconCode + ".png"
        Picasso.get().load(iconUrl).into(binding.weatherIcon)
        binding.rowLayout.setOnClickListener {
//            val action =
//                CityListFragmentDirections.actionCityListFragmentToMapsFragment(weatherCity,
//                    cityList.toTypedArray()
//                )
//            binding.root.findNavController().navigate(action)
        }
    }

    override fun unbindView(binding: CityWeatherItemBinding) {
        binding.weatherIcon.setImageDrawable(null)
        binding.cityNameTextview.text = null
        binding.minTemperatureText.text = null
        binding.maxTemperatureText.text = null
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): CityWeatherItemBinding {
        return CityWeatherItemBinding.inflate(inflater, parent, false)
    }
}

class DragHandleTouchEvent(val action: (position: Int) -> Unit) : TouchEventHook<BindingWeatherInCityItem>() {
//    override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
//        //return if (viewHolder is BindingWeatherInCityItem.ViewHolder) viewHolder.dragHandle else null
//    }

    override fun onTouch(
        v: View,
        event: MotionEvent,
        position: Int,
        fastAdapter: FastAdapter<BindingWeatherInCityItem>,
        item: BindingWeatherInCityItem
    ): Boolean {
        return if (event.action == MotionEvent.ACTION_DOWN) {
            action(position)
            true
        } else {
            false
        }
    }
}




