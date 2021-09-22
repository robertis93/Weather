package com.rob.weather.generaldaytoday.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rob.weather.R
import com.rob.weather.datasource.retrofit.WeatherDataSource
import com.rob.weather.model.FullWeatherToday
import com.rob.weather.model.SortedByDateWeatherForecastResult
import com.rob.weather.model.WeatherForecastResult
import com.rob.weather.model.WeatherToday
import com.rob.weather.utils.Utils.fullDateFormat
import com.rob.weather.utils.Utils.shortDateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class GeneralDayTodayViewModel(val dataSource: WeatherDataSource) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    private val _sortedWeatherForecastResult =
        MutableLiveData<List<SortedByDateWeatherForecastResult>>()
    val sortedWeatherForecastResult: LiveData<List<SortedByDateWeatherForecastResult>> =
        _sortedWeatherForecastResult
    private val _weatherToday = MutableLiveData<WeatherToday>()
    val weatherToday: LiveData<WeatherToday> = _weatherToday
    private val _fullWeatherTodayResponse =
        MutableLiveData<FullWeatherToday>()
    val fullWeatherTodayResponse: LiveData<FullWeatherToday> =
        _fullWeatherTodayResponse

    fun getAllWeatherForecast(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val weatherForecast = dataSource.getWeatherForecastResponse(city)
                withContext(Dispatchers.Main) {
                    getShortAndFullWeatherToday(weatherForecast)
                    getWithoutFirstElementSortedByDateForecastResponseList(weatherForecast)
                    updateFullWeatherTodayResponse(weatherForecast)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = R.string.error.toString()
                }
            }
        }
    }

    private fun getShortAndFullWeatherToday(weatherForecast: WeatherForecastResult) {
        val date = " Сегодня, " + weatherForecast.list.first().date.changeDateFormat()
        val cityName: String = weatherForecast.city.name
        val temperature: String =
            weatherForecast.list.first().main.temp.toInt()
                .toString() + "°"
        val description: String =
            weatherForecast.list.first().weather.first().description
                .replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                    else it.toString()
                } + ", ощущается как  " + weatherForecast.list.first().main.temp_max.toInt()
                .toString() + "°"
        val iconCode = weatherForecast.list.first().weather.first().icon
        val windSpeed = weatherForecast.list.first().wind.speed
        val humidity = weatherForecast.list.first().main.humidity.toInt()
        val precipitation = weatherForecast.list.first().clouds.all
        val forecastResponseList = weatherForecast.list

        val todayWeather = WeatherToday(date, cityName, temperature, description, iconCode)
        val fullTodayWeather = FullWeatherToday(
            date,
            cityName,
            temperature,
            description,
            iconCode,
            windSpeed.toInt(),
            humidity,
            precipitation,
            forecastResponseList
        )
        _fullWeatherTodayResponse.value = fullTodayWeather

        _weatherToday.value = todayWeather
        _weatherToday.value!!.city = _weatherToday.value!!.city.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
    }

    private fun geWeatherForecastResponseGroupByDate(
        weatherForecast: WeatherForecastResult
    ): List<SortedByDateWeatherForecastResult> {
        val weatherForecastGroup = weatherForecast.list.groupBy { it.date.changeDateFormat() }
        return weatherForecastGroup.map { (date, forecasts) ->
            SortedByDateWeatherForecastResult(date, forecasts)}
    }

    private fun getWithoutFirstElementSortedByDateForecastResponseList(
        weatherForecast: WeatherForecastResult
    ) {
        val sortedByDateForecastResponseList =
            geWeatherForecastResponseGroupByDate(weatherForecast)
        val withoutFirstElementSortedByDateForecastResponseList =
            sortedByDateForecastResponseList.toMutableList()
                .subList(1, sortedByDateForecastResponseList.size)
        _sortedWeatherForecastResult.postValue(withoutFirstElementSortedByDateForecastResponseList)
    }

    private fun updateFullWeatherTodayResponse(weatherForecast: WeatherForecastResult) {
        val firstElementWeatherForecastResponse =
            geWeatherForecastResponseGroupByDate(weatherForecast)[0]
        val todayForecastResponseList = firstElementWeatherForecastResponse.forecastResponseList
        _fullWeatherTodayResponse.value?.forecastResponseList = todayForecastResponseList
    }
}

fun String.changeDateFormat(): String {
    val changedDate = fullDateFormat.parse(this)
    return shortDateFormat.format(changedDate)
}





