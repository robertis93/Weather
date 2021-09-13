package com.rob.weather.citylist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rob.weather.citylist.database.WeatherDataBase
import com.rob.weather.citylist.database.WeatherRepository
import com.rob.weather.citylist.model.City
import com.rob.weather.citylist.model.WeatherCity
import com.rob.weather.datasource.retrofit.WeatherDataFromRemoteSource
import com.rob.weather.model.WeatherForecastResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CityListViewModel(application: Application) : AndroidViewModel(application) {
    //val dataSource: WeatherDataSource

    private val repository: WeatherRepository
    private val _cityList = MutableLiveData<List<City>>()
    val cityList: LiveData<List<City>> = _cityList
    val dataSource = WeatherDataFromRemoteSource()
    private val _weatherCityList = MutableLiveData<List<WeatherCity>>()
    val weatherCityList: LiveData<List<WeatherCity>> = _weatherCityList

    init {
        val wordsDao = WeatherDataBase.getDataBase(application).cityDao()

        repository = WeatherRepository(wordsDao, dataSource)
        viewModelScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                _cityList.value = repository.getAllCities()
            }
            getAllWeatherForecast(cityList.value)

        }
    }

    fun getAllWeatherForecast(city: List<City>?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (city != null) {
                for (oneCity in city) {
                    try {
                        val weatherForecastResult = repository.getWeatherResponse(oneCity.name)
                        // val weatherForecastResult = dataSource.getWeatherForecastResponse(city)
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

    private fun getWeatherCity(weatherForecastResult: WeatherForecastResult) {
        val cityName = weatherForecastResult.city.name
        val tempMax = weatherForecastResult.list.first().main.temp_max
        val tempMin = weatherForecastResult.list.first().main.temp_min
        val icon = weatherForecastResult.list.first().weather.first().icon
        val weatherCity = WeatherCity(cityName, tempMax.toInt(), tempMin.toInt(), icon)
        val weatherList = _weatherCityList.value?.toMutableList() ?: mutableListOf()
        //  weatherList.add(weatherCity)
        _weatherCityList.value = weatherList
        _weatherCityList.value?.let { listAlarm ->
            val measureMutableList = listAlarm.toMutableList()
            measureMutableList.add(weatherCity)
            _weatherCityList.value = measureMutableList
            //  _weatherCityList.value = listOf(weatherCity)
        }

        // _weatherCityList.value = listOf(weatherCity)
    }

    fun addCity(cityName: String) {
        try {
            val city = City(cityName)
            _cityList.value?.let { listCity ->
                val cityMutableList = listCity.toMutableList()
                cityMutableList.add(city)
                _cityList.value = cityMutableList
                getAllWeatherForecast(listOf(city))
                viewModelScope.launch {
                    repository.insert(city)
                }
            }
        } catch (e: Exception) {
        }
    }

    fun deleteCity(pos: Int) {
        _weatherCityList.value?.let { listCity ->
            val cityMutableList = listCity.toMutableList()
            val cityName = cityMutableList[pos]
            cityMutableList.removeAt(pos)
            _weatherCityList.value = cityMutableList
            viewModelScope.launch {
                val city = City(cityName.name)
                repository.deleteCity(city)
            }
        }
    }
}

//class CityListModelFactory @Inject constructor(private val repository: WeatherRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(CityListViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return CityListViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
//class WordViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(CityListViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return CityListViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}