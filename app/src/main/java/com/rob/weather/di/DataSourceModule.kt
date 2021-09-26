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
class CityListModule(val application: App) {

    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Utils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideRetrofitService(retrofit: Retrofit): RetrofitServices {
        return retrofit.create(RetrofitServices::class.java)
    }

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
    fun provideRepository(
        cityDao: CityDao,
        weatherDataSource: WeatherDataFromRemoteSource
    ): WeatherRepository {
        return WeatherRepository(cityDao, weatherDataSource)
    }

    @Provides
    fun provideApp(): App {
        return application
    }
}
