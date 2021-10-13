package com.rob.weather.di

import android.content.Context
import com.rob.weather.App
import com.rob.weather.citylist.database.CityDao
import com.rob.weather.citylist.database.WeatherDataBase
import com.rob.weather.citylist.database.WeatherRepository
import com.rob.weather.datasource.retrofit.RetrofitServices
import com.rob.weather.datasource.retrofit.WeatherDataFromRemoteSource
import com.rob.weather.utils.Utils
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class DataBaseModule(val application: App) {

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
