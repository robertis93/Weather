package com.rob.weather.utils.extensions

import android.app.Activity
import com.rob.weather.di.WeatherAppComponent

fun Activity.getAppComponent(): WeatherAppComponent = application.getComponent()
