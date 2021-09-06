package com.rob.weather.di

import android.content.Context
import androidx.room.Room
import com.rob.weather.datasource.localdatasource.WeatherDataBase
import com.rob.weather.datasource.retrofit.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataSourceModule {
    @Provides
    fun provideDataSource(some2: Some2): DataSource{
        return RemoteDataSource(some2)
    }

    @Singleton
    @Provides
    fun provideSome3(): Some3{
        return Some3Impl2()
    }

    @Provides
    fun provideWetherDataBase(@ApplicationContext context: Context): WeatherDataBase{
        return Room.databaseBuilder(context, WeatherDataBase::class.java, "db").build()
    }
}