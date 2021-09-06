package com.rob.weather.di

import com.rob.weather.datasource.retrofit.DataSource
import com.rob.weather.datasource.retrofit.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DataSourceModule {
    @Provides
    fun provideDataSource(): DataSource {
        return RemoteDataSource()
    }
}