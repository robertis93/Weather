package com.rob.weather.generaldaytoday.viewmodel

import android.content.Context
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
    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()
    private val _sortedWeatherForecastResult =
        MutableLiveData<List<SortedByDateWeatherForecastResult>>()
    val sortedWeatherForecastResult: LiveData<List<SortedByDateWeatherForecastResult>> =
        _sortedWeatherForecastResult
    private val _firstSortedWeatherForecastResult =
        MutableLiveData<List<SortedByDateWeatherForecastResult>>()
    val firstSsortedWeatherForecastResult: LiveData<List<SortedByDateWeatherForecastResult>> =
        _firstSortedWeatherForecastResult
    private val _weatherToday = MutableLiveData<WeatherToday>()
    val weatherToday: LiveData<WeatherToday> = _weatherToday

    fun getAllWeatherForecast(context: Context, city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val weatherForecast = dataSource.getWeatherForecastResponse(city)
                if (weatherForecast != null) {
                    withContext(Dispatchers.Main) {
                        weatherToday(context, weatherForecast)
                        withoutFirstElementSortedByDateForecastResponseList(weatherForecast)
                        updateFullWeatherTodayResponse(weatherForecast)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = context.getString(R.string.error_server)
                }
            }
        }
    }

    private fun weatherToday(context: Context, weatherForecast: WeatherForecastResult) {
        val weatherDate = weatherForecast.list.first()
        val date =
            context.getString(R.string.today_with_comma) + weatherDate.date.changeDateFormat()
        val cityName: String = weatherForecast.city.name
        val temperature: String =
            weatherDate.main.temp.toInt()
                .toString() + context.getString(R.string.celsius_icon)
        val description: String =
            weatherDate.weather.first().description
                .replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                    else "$it"
                } + context.getString(R.string.feels_like) + weatherDate.main.temp_max.toInt()
                .toString() + context.getString(R.string.celsius_icon)
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





