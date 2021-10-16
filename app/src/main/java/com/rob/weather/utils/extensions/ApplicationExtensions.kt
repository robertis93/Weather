package com.rob.weather.utils.extensions

import android.app.Application
import com.rob.weather.App
import com.rob.weather.di.WeatherAppComponent

fun Application.getComponent(): WeatherAppComponent {
    if (this !is App) throw IllegalArgumentException("Для работы метода его надо вызывать у класса App.")

    return component
}