package com.rob.weather.generaldaytoday.fragment

import com.rob.weather.generaldaytoday.repository.WeatherForecastRepository
import com.rob.weather.generaldaytoday.retrofit.RemoteDataSource
import com.rob.weather.generaldaytoday.viewmodel.GeneralDayTodayViewModel
import com.squareup.picasso.Picasso
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
