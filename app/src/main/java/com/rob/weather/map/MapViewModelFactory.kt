package com.rob.weather.generaldaytoday.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rob.weather.citylist.database.WeatherRepository
import com.rob.weather.datasource.retrofit.RetrofitServices
import com.rob.weather.map.fragment.MapViewModel
import javax.inject.Inject

class MapViewModelFactory @Inject constructor(private val repository: WeatherRepository, private val retrofitService: RetrofitServices) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            MapViewModel(this.repository, this.retrofitService) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}