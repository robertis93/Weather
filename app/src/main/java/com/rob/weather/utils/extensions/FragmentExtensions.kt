package com.rob.weather.utils.extensions

import androidx.fragment.app.Fragment
import com.rob.weather.di.WeatherAppComponent

fun Fragment.getAppComponent(): WeatherAppComponent = requireActivity().getAppComponent()