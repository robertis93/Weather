package com.rob.weather.di

import com.rob.weather.datasource.retrofit.WeatherDataFromRemoteSource
import com.rob.weather.datasource.retrofit.WeatherDataSource
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
class DataSourceModule {

   // @IntoSet
    @Provides
    fun provideDataSource(): WeatherDataSource {
        return WeatherDataFromRemoteSource()
    }
}