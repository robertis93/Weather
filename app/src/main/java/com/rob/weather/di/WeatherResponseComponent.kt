package com.rob.weather.di

import com.rob.weather.generaldaytoday.fragment.GeneralDayTodayFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [WeatherForecastModule::class])
@Singleton
interface WeatherAppComponent {
         fun inject(generalDayTodayFragment: GeneralDayTodayFragment)

}










//@Singleton
//@Component(modules = [APIModule::class])
//interface APIComponent {
//    fun inject(repository: WeatherForecastRepository)
//    fun inject(generalDayTodayViewModel: GeneralDayTodayViewModel)
//    fun inject(retroViewModelFactory:GeneralDayTodayViewModelFactory)
//fun getPicasso(): Picasso?
//}
