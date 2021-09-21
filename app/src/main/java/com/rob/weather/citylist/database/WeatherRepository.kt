package com.rob.weather.citylist.database

import com.rob.weather.citylist.model.City
import com.rob.weather.datasource.retrofit.WeatherDataSource
import com.rob.weather.model.WeatherForecastResult
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val cityDao: CityDao, val dataSource:
WeatherDataSource) {

    suspend fun getAllCities() : List<City> =
        cityDao.getListCity()

    suspend fun insert(cityName: City) {
        cityDao.addCity(cityName)
    }

    suspend fun getWeatherResponse(city: String): WeatherForecastResult {
        val weatherForecastResult = dataSource.getWeatherForecastResponse(city)
        return weatherForecastResult
    }

    suspend fun deleteCity(city: City) {
        cityDao.deleteCity(city)
    }
}