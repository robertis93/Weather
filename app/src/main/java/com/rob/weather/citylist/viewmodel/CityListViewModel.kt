package com.rob.weather.citylist.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.rob.weather.R
import com.rob.weather.citylist.database.CityDao
import com.rob.weather.citylist.database.WeatherDataBase
import com.rob.weather.citylist.model.City
import com.rob.weather.citylist.model.WeatherCity
import com.rob.weather.datasource.retrofit.WeatherDataSource
import com.rob.weather.model.WeatherForecastResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CityListViewModel(private val dataSource: WeatherDataSource, private val cityDao: CityDao) : ViewModel() {

    private val _cityList = MutableLiveData<List<City>>()
    val cityList: LiveData<List<City>> = _cityList
   // var cityDao: CityDao = WeatherDataBase.getDataBase(application).cityDao()
    

    private val _weatherCityList = MutableLiveData<List<WeatherCity>>()
    val weatherCityList: LiveData<List<WeatherCity>> = _weatherCityList

    init {
        getListAlarm()
        for(oneCity in cityList.value!!){
            getAllWeatherForecast(oneCity.name)
        }
    }



    fun getAllWeatherForecast(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val weatherForecastResult = dataSource.getWeatherForecastResponse(city)
                withContext(Dispatchers.Main) {
                    weatherForecastResult.let { getWeatherCity(it)}
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                }
            }
        }
    }

    private fun getWeatherCity(weatherForecastResult: WeatherForecastResult) {
        val cityName = weatherForecastResult.city.name
        val tempMax = weatherForecastResult.list.first().main.temp_max
        val tempMin = weatherForecastResult.list.first().main.temp_min
        val icon = weatherForecastResult.list.first().weather.first().icon
        val weatherCity = WeatherCity(cityName, tempMax.toInt(), tempMin.toInt(), icon)
        _weatherCityList.value?.let { listAlarm ->
            val measureMutableList = listAlarm.toMutableList()
            measureMutableList.add(weatherCity)
            _weatherCityList.value = measureMutableList
            // weatherCityList.value = weatherCity
        }
    }

    private fun getListAlarm() {
        viewModelScope.launch {
            _cityList.value = cityDao.getListCity()
        }
    }

    fun addCity(cityName: String) {
        val city = City(cityName)
        _cityList.value?.let { listCity ->
            val cityMutableList = listCity.toMutableList()
            cityMutableList.add(city)
            _cityList.value = cityMutableList
            viewModelScope.launch {
                cityDao.addCity(city)
            }
        }
    }

    fun deleteCity(pos: Int) {
        _cityList.value?.let { listCity ->
            val cityMutableList = listCity.toMutableList()
            val city = cityMutableList[pos]
            cityMutableList.removeAt(pos)
            _cityList.value = cityMutableList
            viewModelScope.launch {
               cityDao.deleteCity(city)
            }

        }
    }
}