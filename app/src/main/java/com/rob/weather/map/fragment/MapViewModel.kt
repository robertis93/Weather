package com.rob.weather.map.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rob.weather.citylist.database.WeatherRepository
import com.rob.weather.citylist.model.City
import com.rob.weather.citylist.model.WeatherCity
import com.rob.weather.datasource.retrofit.RetrofitServices
import com.rob.weather.datasource.retrofit.WeatherDataFromRemoteSource
import com.rob.weather.model.WeatherForecastResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel(
    private val repository: WeatherRepository,
    retrofitService: RetrofitServices
) : ViewModel() {

    private val _cityList = MutableLiveData<List<City>>()
    val cityList: LiveData<List<City>> = _cityList
    val dataSource = WeatherDataFromRemoteSource(retrofitService)

    fun getWeatherInCity(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val city = repository.getWeatherInCityByCoordinates(latitude, longitude).city.name
                addCity(city)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                }
            }
        }
    }

    fun addCity(cityName: String) {
        try {
            val city = City(cityName)
            _cityList.value?.let { listCity ->
                val cityMutableList = listCity.toMutableList()
                cityMutableList.add(city)
                _cityList.value = cityMutableList
                viewModelScope.launch {
                    repository.insert(city)
                }
            }
        } catch (e: Exception) {
        }
    }
}

