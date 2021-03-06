package com.rob.weather.di

import com.rob.weather.generaldaytoday.fragment.CityListViewModelFactory
import com.rob.weather.generaldaytoday.fragment.GeneralDayTodayViewModelFactory
import com.rob.weather.generaldaytoday.fragment.MapViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ApplicationModule::class, RemoteModule::class, DataBaseModule::class])
@Singleton
interface WeatherAppComponent {
    fun getDependencyGeneralDay(): GeneralDayTodayViewModelFactory
    fun getDependencyCityList(): CityListViewModelFactory
    fun getDependencyMap(): MapViewModelFactory
}

