package com.rob.weather.generaldaytoday.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rob.weather.citylist.database.WeatherRepository
import com.rob.weather.citylist.viewmodel.CityListViewModel
import javax.inject.Inject

class CityListViewModelFactory @Inject constructor(private val repository: WeatherRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CityListViewModel::class.java)) {
            CityListViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}