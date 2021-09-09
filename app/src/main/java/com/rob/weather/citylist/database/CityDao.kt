package com.rob.weather.citylist.database

import androidx.room.*
import com.rob.weather.citylist.model.City

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCity(name: City)

    @Delete
    suspend fun deleteCity(alarm: City)

    @Query("SELECT * FROM city_table")
    suspend fun getListCity(): List<City>
}