package com.rob.weather.generaldaytoday.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rob.weather.citylist.database.WeatherRepository
import com.rob.weather.datasource.retrofit.WeatherDataFromRemoteSource
import com.rob.weather.generaldaytoday.viewmodel.GeneralDayTodayViewModel
import javax.inject.Inject

class GeneralDayTodayViewModelFactory @Inject constructor(
    val dataSource: WeatherDataFromRemoteSource,
    private val repository: WeatherRepository,
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GeneralDayTodayViewModel::class.java)) {
            GeneralDayTodayViewModel(
                this.dataSource,
                this.repository
            ) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}