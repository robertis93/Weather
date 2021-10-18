package com.rob.weather.generaldaytoday.viewmodel

import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rob.weather.R
import com.rob.weather.datasource.retrofit.WeatherDataFromRemoteSource
import com.rob.weather.model.*
import com.rob.weather.utils.Utils.fullDateFormat
import com.rob.weather.utils.Utils.shortDateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class GeneralDayTodayViewModel(val dataSource: WeatherDataFromRemoteSource) : ViewModel() {
    private val _errorMessage =  MutableLiveData<Int>()
    val errorMessage:  LiveData<Int> = _errorMessage
    private val _sortedWeatherForecastResult =
        MutableLiveData<List<SortedByDateWeatherForecastResult>>()
    val sortedWeatherForecastResult: LiveData<List<SortedByDateWeatherForecastResult>> =
        _sortedWeatherForecastResult
    private val _firstSortedWeatherForecastResult =
        MutableLiveData<List<SortedByDateWeatherForecastResult>>()
    val firstSortedWeatherForecastResult: LiveData<List<SortedByDateWeatherForecastResult>> =
        _firstSortedWeatherForecastResult
    private val _weatherToday = MutableLiveData<WeatherToday>()
    val weatherToday: LiveData<WeatherToday> = _weatherToday
    private var _progressBar = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean> = _progressBar

    fun getAllWeatherForecast(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val weatherForecast = dataSource.getWeatherForecastResponse(city)
                if (weatherForecast != null) {
                    withContext(Dispatchers.Main) {
                        _progressBar.value = true
                        weatherToday(weatherForecast)
                        withoutFirstElementSortedByDateForecastResponseList(weatherForecast)
                        updateFullWeatherTodayResponse(weatherForecast)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = R.string.error_server
                    _progressBar.value = false
                }
            }
        }
    }

    private fun weatherToday(weatherForecast: WeatherForecastResult) {
        val weatherDate = weatherForecast.list.first()
        val date = weatherDate.date.changeDateFormat()
        val cityName: String = weatherForecast.city.name
        val temperature: String = weatherDate.main.temp.toInt().toString()
        val description: String =
            weatherDate.weather.first().description
                .replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                    else "$it"
                }
        val iconCode = weatherDate.weather.first().icon
        val todayWeather = WeatherToday(date, cityName, temperature, description, iconCode)
        _weatherToday.value = todayWeather
    }

    private fun geWeatherForecastResponseGroupByDate(
        weatherForecast: WeatherForecastResult
    ): List<SortedByDateWeatherForecastResult> {
        val weatherForecastGroup = weatherForecast.list.groupBy { it.date.changeDateFormat() }
        return weatherForecastGroup.map { (date, forecasts) ->
            SortedByDateWeatherForecastResult(
                date,
                weatherForecast.city.name,
                forecasts
            )
        }
    }

    private fun withoutFirstElementSortedByDateForecastResponseList(
        weatherForecast: WeatherForecastResult
    ) {
        val sortedByDateForecastResponseList =
            geWeatherForecastResponseGroupByDate(weatherForecast)
        val withoutFirstElementSortedByDateForecastResponseList =
            sortedByDateForecastResponseList
                .subList(1, sortedByDateForecastResponseList.size)
        _sortedWeatherForecastResult.postValue(withoutFirstElementSortedByDateForecastResponseList)
    }

    private fun updateFullWeatherTodayResponse(weatherForecast: WeatherForecastResult) {
        val firstElementWeatherForecastResponse =
            geWeatherForecastResponseGroupByDate(weatherForecast)[0]
        _firstSortedWeatherForecastResult.value = listOf(firstElementWeatherForecastResponse)
    }
}

private fun String.changeDateFormat(): String {
    val changedDate = fullDateFormat.parse(this)
    return shortDateFormat.format(changedDate)
}





