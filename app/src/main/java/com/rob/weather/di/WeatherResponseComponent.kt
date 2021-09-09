package com.rob.weather.di

import com.rob.weather.citylist.fragment.CityListFragment
import com.rob.weather.generaldaytoday.fragment.GeneralDayTodayFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [DataSourceModule::class])
@Singleton
interface WeatherAppComponent {
    fun inject(generalDayTodayFragment: GeneralDayTodayFragment)
    fun inject(cityListFragment: CityListFragment)
}

