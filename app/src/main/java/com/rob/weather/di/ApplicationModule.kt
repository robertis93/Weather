package com.rob.weather.di

import android.content.Context
import com.rob.weather.App
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(val application: App) {

    @Provides
    fun provideContext(application: App): Context {
        return application.applicationContext
    }

    @Provides
    fun provideApp(): App {
        return application
    }
}
