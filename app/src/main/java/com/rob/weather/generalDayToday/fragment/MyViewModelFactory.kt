package com.rob.weather.generalDayToday.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rob.weather.generalDayToday.repository.Repository
import com.rob.weather.generalDayToday.viewmodel.GeneralDayTodayViewModel

class MyViewModelFactory constructor(private val repository: Repository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GeneralDayTodayViewModel::class.java)) {
            GeneralDayTodayViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}