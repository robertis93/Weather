package com.rob.weather.citylist.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rob.weather.citylist.database.WeatherRepository
import com.rob.weather.citylist.model.City
import com.rob.weather.citylist.model.WeatherCity
import com.rob.weather.datasource.retrofit.RetrofitServices
import com.rob.weather.datasource.retrofit.WeatherDataFromRemoteSource
import com.rob.weather.model.WeatherForecastResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CityListViewModel(
    private val repository: WeatherRepository,
    retrofitService: RetrofitServices
) : ViewModel() {

    val dataSource = WeatherDataFromRemoteSource(retrofitService)
    private val _cityList = MutableSharedFlow<Set<City>>()
    val cityList: SharedFlow<Set<City>> = _cityList.asSharedFlow()
    private val _cityListWithWeather = MutableSharedFlow<Set<WeatherCity>>()
    val cityListWithWeather: SharedFlow<Set<WeatherCity>> = _cityListWithWeather.asSharedFlow()

//    init {
//        viewModelScope.launch(Dispatchers.IO) {
//            withContext(Dispatchers.Main) {
//                _cityList.emit(repository.getAllCities())
//
//                getAllWeatherForecast(cityList.single())
//                val z = cityList.single()
//            }
//        }
//    }

    fun getCityList() {
        viewModelScope.launch(Dispatchers.IO)
        {
            withContext(Dispatchers.Main) {
                _cityList.emit(repository.getAllCities().toSet())
            }
        }
    }


    fun getAllWeatherForecast(city: List<City>?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (city != null) {
                for (oneCity in city) {
                    try {
                        val weatherForecastResult = repository.getWeatherResponse(oneCity.name)
                        withContext(Dispatchers.Main) {
                            weatherForecastResult.let { getWeatherCity(it) }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                        }
                    }
                }
            }
        }
    }

    fun getWeatherByCity(city: List<City>?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (city != null) {
                for (oneCity in city) {
                    try {
                        val weatherForecastResult = repository.getWeatherResponse(oneCity.name)
                        withContext(Dispatchers.Main) {
                            weatherForecastResult.let { getWeatherCity(it) }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                        }
                    }
                }
            }
        }
    }

    fun getWeatherInCityList() {
        viewModelScope.launch(Dispatchers.IO) {
            for (oneCity in cityList.single()!!) {
                try {
                    val weatherForecastResult = repository.getWeatherResponse(oneCity.name)
                    withContext(Dispatchers.Main) {
                        weatherForecastResult.let { getWeatherCity(it) }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                    }
                }
            }
        }
    }

    private suspend fun getWeatherCity(weatherForecastResult: WeatherForecastResult) {
        val cityName = weatherForecastResult.city.name
        val latitude = weatherForecastResult.city.coordinates.latitude
        val longitude = weatherForecastResult.city.coordinates.longitude
        val tempMax = weatherForecastResult.list.first().main.temp_max
        val tempMin = weatherForecastResult.list.first().main.temp_min
        val icon = weatherForecastResult.list.first().weather.first().icon
        val weatherCity =
            WeatherCity(cityName, tempMax.toInt(), tempMin.toInt(), icon, latitude, longitude)

        _cityListWithWeather.emit(setOf(weatherCity))

        Log.i("myLogs", "getWeatherCity VM")

    }

    fun addCity(cityName: String) {
        try {
            viewModelScope.launch {
                val city = City(cityName)
                repository.insert(city)
                _cityList.emit(repository.getAllCities().toSet())
             //   _cityList.emit(setOf(city))
//                _cityList.single()?.let { listCity ->
//                    val cityMutableList = listCity.toMutableSet()
//                    cityMutableList.add(city)
//                    getAllWeatherForecast(listOf(city))
//                    repository.insert(city)
//                    _cityList.emit(cityMutableList)
//                }
            }
        } catch (e: Exception) {
        }
    }

    fun deleteTheCity(pos: Int) {

        viewModelScope.launch {
            val cityList = repository.getAllCities()
            val cityName = cityList.toMutableList().get(pos)
            val city = City(cityName.name)
            repository.deleteCity(city)
            _cityList.emit(repository.getAllCities().toSet())
        }
    }

    fun deleteCity(pos: Int) {

        viewModelScope.launch {
            val cityMutableList = _cityListWithWeather.asLiveData().value?.toMutableList()
            if (cityMutableList != null) {
                val cityName = cityMutableList.get(pos)
                cityMutableList?.removeAt(pos)
                _cityListWithWeather.emit(cityMutableList.toSet())
                val city = City(cityName.name)
                repository.deleteCity(city)
            }
        }
    }

    fun updateCity(cityList: List<WeatherCity>) {
        val mutableList = mutableListOf<City>()
        try {
            for (cityName in cityList) {
                val city = City(cityName.name)
                mutableList.add(city)
            }
            viewModelScope.launch {
                repository.deleteAllCity()
                repository.insertList(mutableList)
            }
        } catch (e: Exception) {
        }
    }
}


