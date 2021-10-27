package com.rob.weather.di

import android.content.Context
import com.rob.weather.citylist.database.CityDao
import com.rob.weather.citylist.database.WeatherDataBase
import com.rob.weather.citylist.database.WeatherRepository
import com.rob.weather.datasource.retrofit.WeatherDataFromRemoteSource
import dagger.Module
import dagger.Provides

@Module
class DataBaseModule {

    @Provides
    fun provideDB(context: Context): WeatherDataBase {
        return WeatherDataBase.getDataBase(context)
    }

    @Provides
    fun provideCityDao(dataBase: WeatherDataBase): CityDao {
        return dataBase.cityDao()
    }

    @Provides
    fun provideRepository(
        cityDao: CityDao,
        weatherDataSource: WeatherDataFromRemoteSource
    ): WeatherRepository {
        return WeatherRepository(cityDao, weatherDataSource)
    }
}
