package com.rob.weather.di

import com.rob.weather.generaldaytoday.fragment.CityListViewModelFactory
import com.rob.weather.generaldaytoday.fragment.GeneralDayTodayViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Component(modules = [CityListModule::class])
@Singleton
interface WeatherAppComponent {
    fun getDependencyGeneralDay(): GeneralDayTodayViewModelFactory
    fun getDependencyCityList() : CityListViewModelFactory
}

