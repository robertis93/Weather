package com.rob.weather.generaldaytoday.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rob.weather.repository.WeatherForecastRepository
import com.rob.weather.generaldaytoday.viewmodel.GeneralDayTodayViewModel
import javax.inject.Inject

class GeneralDayTodayViewModelFactory @Inject constructor(private val weatherForecastRepository: WeatherForecastRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GeneralDayTodayViewModel::class.java)) {
            GeneralDayTodayViewModel(this.weatherForecastRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}