package com.rob.weather.mainactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rob.weather.App
import com.rob.weather.citylist.database.WeatherRepository
import com.rob.weather.datasource.retrofit.WeatherDataFromRemoteSource
import javax.inject.Inject

class MainActivityViewModelFactory @Inject constructor(
    val dataSource: WeatherDataFromRemoteSource,
    private val repository: WeatherRepository,
    private val app: App
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            MainActivityViewModel(
                this.dataSource,
                this.repository,
                this.app
            ) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}