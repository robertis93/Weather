package com.rob.weather.di

import com.rob.weather.repository.WeatherForecastRepository
import com.rob.weather.generaldaytoday.viewmodel.GeneralDayTodayViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class WeatherForecastModule {

    @Provides
    @Singleton
    fun getViewModel(weatherForecastRepository: WeatherForecastRepository) : GeneralDayTodayViewModel {
        return GeneralDayTodayViewModel(weatherForecastRepository)
    }
}















//
//    var id_key = "2e65127e909e178d0af311a81f39948c"
//
//    @Provides
//    fun retrofitService() : RemoteDataSource.RetrofitServices {
//        return Retrofit.Builder()
//            .baseUrl("https://api.openweathermap.org/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(RemoteDataSource.RetrofitServices::class.java)
//    }
//
//    fun getRepository(): WeatherForecastRepository? {}
//

//
//    fun getId(): String {}
//
//    @Provides
//    fun picasso(context: Context?): Picasso? {
//        return context?.let { Picasso.Builder(it).build() }
//    }
//
//
////    @Singleton
////    @Provides
////    fun provideApi(remoteDataSource: RemoteDataSource){
////        return remoteDataSource
////
////    }
//
////    @Singleton
////    @Provides
////    fun provideId() : String{
////        return String()
////
////    }
////
////    @Provides
////    open fun retrofit(
////        okHttpClient: OkHttpClient?,
////        gsonConverterFactory: GsonConverterFactory?, gson: Gson?
////    ): Retrofit? {
////        return Retrofit.Builder()
////            .client(okHttpClient)
////            .baseUrl("https://randomuser.me/")
////            .addConverterFactory(gsonConverterFactory)
////            .build()
////    }
//}