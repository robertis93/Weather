package com.rob.weather.citylist.database

import com.rob.weather.citylist.model.City
import com.rob.weather.datasource.retrofit.WeatherDataFromRemoteSource
import com.rob.weather.model.WeatherForecastResult
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val cityDao: CityDao, val dataSource:
    WeatherDataFromRemoteSource
) {

    suspend fun getAllCities(): List<City> =
        cityDao.getListCity()

    suspend fun insert(cityName: City) {
        cityDao.addCity(cityName)
    }

    suspend fun insertList(cityList: List<City>) {
        cityDao.addCityList(cityList)
    }

    suspend fun update(cityList: List<City>) {
        cityDao.updateCity(cityList)
    }

    suspend fun getWeatherResponse(city: String): WeatherForecastResult {
        val weatherForecastResult = dataSource. getWeatherForecastResponse(city)
        return weatherForecastResult
    }

    suspend fun getWeatherInCityByCoordinates(latitude: Double, longitude: Double):
            WeatherForecastResult {
        return dataSource.getWeatherInformationByLatitudeAndLongitude(latitude, longitude)
    }

    suspend fun deleteCity(city: City) {
        cityDao.deleteCity(city)
    }

    suspend fun deleteAllCity() {
        cityDao.deleteAllCity()
    }
}