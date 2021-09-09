package com.rob.weather.generaldaytoday.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rob.weather.citylist.database.CityDao
import com.rob.weather.citylist.viewmodel.CityListViewModel
import com.rob.weather.datasource.retrofit.WeatherDataSource
import com.rob.weather.generaldaytoday.viewmodel.GeneralDayTodayViewModel
import javax.inject.Inject

class CityListViewModelFactory @Inject constructor (val dataSource: WeatherDataSource, val cityDao: CityDao) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CityListViewModel::class.java)) {
            CityListViewModel(
                this.dataSource,
                this.cityDao
            ) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}