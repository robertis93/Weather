package com.rob.weather.selectedday

import androidx.lifecycle.ViewModel
import com.rob.weather.datasource.localdatasource.WeatherDataBase
import com.rob.weather.datasource.retrofit.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SelectedDayViewModel @Inject constructor(
    val db: WeatherDataBase,
    val dataSource: DataSource
) : ViewModel() {
    /*val some3 = Some3Impl2()
    val some2 = Some2(some3)
    val dataSource = RemoteDataSource(some2)*/
}



class Some @Inject constructor(
    val dataSource: DataSource
){
    /*val some3 = Some3Impl2()
    val some2 = Some2(some3)
    val dataSource = RemoteDataSource(some2)*/
}