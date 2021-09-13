package com.rob.weather.citylist.database

import com.rob.weather.citylist.model.City
import com.rob.weather.datasource.retrofit.WeatherDataSource
import com.rob.weather.model.WeatherForecastResult

class WeatherRepository(private val cityDao: CityDao) {
//val dataSource: WeatherDataSource

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    // val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()

    suspend fun getAllCities() : List<City> =
        cityDao.getListCity()
  //  val allCities = cityDao.getListCity()

    suspend fun insert(cityName: City) {
        cityDao.addCity(cityName)
    }

   // suspend fun getWeatherResponse(city: String): WeatherForecastResult {
     //   val weatherForecastResult = dataSource.getWeatherForecastResponse(city)
     //   return weatherForecastResult
  //  }

    suspend fun deleteCity(city: City) {
        cityDao.deleteCity(city)
    }
}