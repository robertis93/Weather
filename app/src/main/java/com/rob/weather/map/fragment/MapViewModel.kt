package com.rob.weather.map.fragment

import androidx.lifecycle.ViewModel
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel(
    private val repository: WeatherRepository,
    retrofitService: RetrofitServices
) : ViewModel() {

    private val _cityList = MutableSharedFlow<List<City>>()
    val cityList: SharedFlow<List<City>> = _cityList.asSharedFlow()
    val dataSource = WeatherDataFromRemoteSource(retrofitService)
    private val _weatherCity = MutableSharedFlow<WeatherCity>()
    val weatherCity: SharedFlow<WeatherCity> = _weatherCity.asSharedFlow()

    private val _cityListWithWeather = MutableSharedFlow<List<WeatherCity>>()
    val cityListWithWeather: SharedFlow<List<WeatherCity>> = _cityListWithWeather.asSharedFlow()

    fun getCityList() {
        viewModelScope.launch(Dispatchers.IO)
        {
            withContext(Dispatchers.Main) {
                _cityList.emit(repository.getAllCities())
            }
        }
    }

    fun getWeatherByCity(city: List<City>?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (city != null) {
                val measureMutableList = emptyList<WeatherCity>().toMutableList()
                for (oneCity in city) {
                    try {
                        val weatherForecastResult = repository.getWeatherResponse(oneCity.name)
                        withContext(Dispatchers.Main) {
                            val weatherInCity = getWeatherCity(weatherForecastResult)
                            measureMutableList.add(weatherInCity)
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                        }
                    }
                }
                _cityListWithWeather.emit(measureMutableList)
            }
        }
    }

    private fun getWeatherCity(weatherForecastResult: WeatherForecastResult): WeatherCity {
        val cityName = weatherForecastResult.city.name
        val latitude = weatherForecastResult.city.coordinates.latitude
        val longitude = weatherForecastResult.city.coordinates.longitude
        val tempMax = weatherForecastResult.list.first().main.temp_max
        val tempMin = weatherForecastResult.list.first().main.temp_min
        val icon = weatherForecastResult.list.first().weather.first().icon
        val weatherCity =
            WeatherCity(cityName, tempMax.toInt(), tempMin.toInt(), icon, latitude, longitude)
        return weatherCity
    }

    suspend fun getWeatherInCity(latitude: Double, longitude: Double): WeatherCity {
        val weatherForecastResult = repository.getWeatherInCityByCoordinates(latitude, longitude)
        val cityName = weatherForecastResult.city.name
        addCity(cityName)
        val latitude = weatherForecastResult.city.coordinates.latitude
        val longitude = weatherForecastResult.city.coordinates.longitude
        val tempMax = weatherForecastResult.list.first().main.temp_max
        val tempMin = weatherForecastResult.list.first().main.temp_min
        val icon = weatherForecastResult.list.first().weather.first().icon
        val weatherCity =
            WeatherCity(cityName, tempMax.toInt(), tempMin.toInt(), icon, latitude, longitude)
        return weatherCity
    }

    suspend fun addCity(cityName: String) {
        try {
            val city = City(cityName)
            repository.insert(city)
        } catch (e: Exception) {
        }
    }
}

