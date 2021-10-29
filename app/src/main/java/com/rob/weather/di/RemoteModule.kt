package com.rob.weather.di

import com.rob.weather.App
import com.rob.weather.datasource.retrofit.RetrofitServices
import com.rob.weather.datasource.retrofit.RetrofitServicesIP
import com.rob.weather.utils.BASE_URL
import com.rob.weather.utils.URL_TO_GET_IP
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class RemoteModule(val application: App) {

    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideRetrofitService(retrofit: Retrofit): RetrofitServices {
        return retrofit.create(RetrofitServices::class.java)
    }

    @Provides
    fun provideRetrofitServiceIP(retrofit: Retrofit): RetrofitServicesIP {
        return retrofit.create(RetrofitServicesIP::class.java).receiveCityByIp(URL_TO_GET_IP)
    }
}
