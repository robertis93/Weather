package com.rob.weather.citylist.database

import androidx.lifecycle.LiveData
import com.rob.weather.citylist.model.City

class WeatherRepository(private val cityDao: CityDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
   // val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()

    suspend fun insert(word: City) {
        cityDao.addCity(word)
    }
}