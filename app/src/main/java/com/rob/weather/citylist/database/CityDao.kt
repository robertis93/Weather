package com.rob.weather.citylist.database

import androidx.room.*
import com.rob.weather.citylist.model.City

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCity(name: City)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCityList(cityList: List<City>)

    @Update
    suspend fun updateCity(cityList: List<City>)

    @Delete
    suspend fun deleteCity(name: City)

    @Query("DELETE FROM city_table")
    suspend fun deleteAllCity()

    @Query("SELECT * FROM city_table")
    suspend fun getListCity(): List<City>
}