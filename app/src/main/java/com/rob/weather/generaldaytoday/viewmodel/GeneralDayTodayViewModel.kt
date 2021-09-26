package com.rob.weather.generaldaytoday.viewmodel

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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class GeneralDayTodayViewModel(val dataSource: WeatherDataFromRemoteSource) : ViewModel() {
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
                    weatherToday(weatherForecast)
                    withoutFirstElementSortedByDateForecastResponseList(weatherForecast)
                    updateFullWeatherTodayResponse(weatherForecast)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = R.string.error.toString()
                }
            }
        }
    }

    private fun weatherToday(weatherForecast: WeatherForecastResult) {
        val weatherDate = weatherForecast.list.first()
        val date = " Сегодня, " + weatherDate.date.changeDateFormat()
        val cityName: String = weatherForecast.city.name
        val temperature: String =
            weatherDate.main.temp.toInt()
                .toString() + "°"
        val description: String =
            weatherDate.weather.first().description
                .replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                    else "$it"
                } + ", ощущается как  " + weatherDate.main.temp_max.toInt()
                .toString() + "°"
        val iconCode = weatherDate.weather.first().icon
        val windSpeed = weatherDate.wind.speed
        val humidity = weatherDate.main.humidity
        val precipitation = weatherDate.clouds.all
        val forecastResponseList = weatherForecast.list
        shortWeatherToday(date, cityName, temperature, description, iconCode)

        fullWeatherToday(
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
    }

    private fun shortWeatherToday(
        date: String,
        cityName: String,
        temperature: String,
        description: String,
        iconCode: String
    ) {
        val todayWeather = WeatherToday(date, cityName, temperature, description, iconCode)
        _weatherToday.value = todayWeather
    }

    private fun fullWeatherToday(
        date: String,
        cityName: String,
        temperature: String,
        description: String,
        iconCode: String,
        windSpeed: Int,
        humidity: Int,
        precipitation: Int,
        forecastResponseList: List<ForecastResponse>
    ) {
        val fullTodayWeather = FullWeatherToday(
            date,
            cityName,
            temperature,
            description,
            iconCode,
            windSpeed,
            humidity,
            precipitation,
            forecastResponseList
        )
        _fullWeatherTodayResponse.value = fullTodayWeather
    }

    private fun geWeatherForecastResponseGroupByDate(
        weatherForecast: WeatherForecastResult
    ): List<SortedByDateWeatherForecastResult> {
        val weatherForecastGroup = weatherForecast.list.groupBy { it.date.changeDateFormat() }
        return weatherForecastGroup.map { (date, forecasts) ->
            SortedByDateWeatherForecastResult(date, forecasts)
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
        val todayForecastResponseList = firstElementWeatherForecastResponse.forecastResponseList
        _fullWeatherTodayResponse.value?.forecastResponseList = todayForecastResponseList
    }
}

private fun String.changeDateFormat(): String {
    val changedDate = fullDateFormat.parse(this)
    return shortDateFormat.format(changedDate)
}





