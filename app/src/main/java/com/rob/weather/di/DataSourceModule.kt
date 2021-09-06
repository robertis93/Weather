package com.rob.weather.di

import com.rob.weather.datasource.retrofit.DataSource
import com.rob.weather.datasource.retrofit.RemoteDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataSourceModule {
    @Provides
    @Singleton
    fun provideDataSource(): DataSource {
        return RemoteDataSource()
    }
}