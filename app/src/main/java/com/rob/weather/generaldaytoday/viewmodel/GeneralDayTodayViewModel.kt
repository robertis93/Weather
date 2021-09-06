package com.rob.weather.generaldaytoday.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rob.weather.R
import com.rob.weather.datasource.retrofit.DataSource
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
import javax.inject.Inject

class GeneralDayTodayViewModel @Inject constructor(
    val dataSource: DataSource,
) : ViewModel() {

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
                val weatherForecastResult = dataSource.getWeatherForecastResponse(city)
                withContext(Dispatchers.Main) {
                    _errorMessage.value = R.string.error.toString()
                    weatherForecastResult?.let { getShortAndFullWeatherToday(it) }
                    weatherForecastResult?.let {
                        getWithoutFirstElementAndUpdateFirstElementSortedByDateForecastResponseList(
                            it
                        )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "Ошибка сервера"
                }
            }
        }
    }

    private fun getShortAndFullWeatherToday(weatherForecastResult: WeatherForecastResult) {
        val date = " Сегодня, " + weatherForecastResult.list.first().date.changeDateFormat()
        val cityName: String = weatherForecastResult.city.name
        val temperature: String =
            weatherForecastResult.list.first().main.temp.toInt()
                .toString() + "°"
        val description: String =
            weatherForecastResult.list.first().weather.first().description
                .replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                    else it.toString()
                } + ", ощущается как  " + weatherForecastResult.list.first().main.temp_max.toInt()
                .toString() + "°"
        val iconCode = weatherForecastResult.list.first().weather.first().icon
        val windSpeed = weatherForecastResult.list.first().wind.speed
        val humidity = weatherForecastResult.list.first().main.humidity.toInt()
        val precipitation = weatherForecastResult.list.first().clouds.all
        val forecastResponseList = weatherForecastResult.list

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

    private fun geWeatherForecastResponseGroupByDate(weatherForecastResult: WeatherForecastResult):
            List<SortedByDateWeatherForecastResult> {
        val weatherForecastGroup =
            weatherForecastResult.list.groupBy { (it.date).changeDateFormat() }
        return (weatherForecastGroup.keys).map { date ->
            val forecasts = weatherForecastGroup[date] ?: emptyList()
            SortedByDateWeatherForecastResult(date, forecasts)
        }
    }

    private fun getWithoutFirstElementAndUpdateFirstElementSortedByDateForecastResponseList(
        weatherForecastResult:
        WeatherForecastResult
    ) {
        val sortedByDateForecastResponseList =
            geWeatherForecastResponseGroupByDate(weatherForecastResult)
        val withoutFirstElementSortedByDateForecastResponseList =
            sortedByDateForecastResponseList.toMutableList()
                .subList(1, sortedByDateForecastResponseList.size)
        _sortedWeatherForecastResult.postValue(withoutFirstElementSortedByDateForecastResponseList)
        val firstElementWeatherForecastResponse = geWeatherForecastResponseGroupByDate(weatherForecastResult)[0]
        val todayForecastResponseList = firstElementWeatherForecastResponse.forecastResponseList
        _fullWeatherTodayResponse.value?.forecastResponseList = todayForecastResponseList
    }
}

fun String.changeDateFormat(): String {
    val changedDate = fullDateFormat.parse(this)
    return shortDateFormat.format(changedDate)
}





