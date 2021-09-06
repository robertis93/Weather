package com.rob.weather

import android.app.Application
import com.rob.weather.di.WeatherAppComponent
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application()