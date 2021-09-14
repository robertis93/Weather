package com.rob.weather.di

import android.content.Context
import com.rob.weather.App
import com.rob.weather.citylist.database.CityDao
import com.rob.weather.citylist.database.WeatherDataBase
import com.rob.weather.citylist.database.WeatherRepository
import com.rob.weather.datasource.retrofit.WeatherDataFromRemoteSource
import com.rob.weather.datasource.retrofit.WeatherDataSource
import dagger.Module
import dagger.Provides


@Module
class DataSourceModule {

    // @IntoSet
    @Provides
    fun provideDataSource(): WeatherDataSource {
        return WeatherDataFromRemoteSource()
    }
}

@Module
class CityListModule {
//    // @IntoSet
//    @Provides
//    fun provideDataSource(): WeatherDataSource {
//        return WeatherDataFromRemoteSource()
//    }

    @Provides
    fun provideContext(application: App): Context {
        return application.applicationContext
    }

    @Provides
    fun provideDB(context: Context): WeatherDataBase {
        return WeatherDataBase.getDataBase(context)
    }

    @Provides
    fun provideCityDao(dataBase: WeatherDataBase): CityDao {
        return dataBase.cityDao()
    }

    @Provides
    fun provideRepository(cityDao: CityDao, weatherDataSource: WeatherDataSource) : WeatherRepository{
        return WeatherRepository(cityDao, weatherDataSource)
    }
}
