package com.rob.weather.generaldaytoday.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rob.weather.R
import com.rob.weather.utils.Utils.changeDateFormat
import com.rob.weather.generaldaytoday.repository.WeatherForecastRepository
import com.rob.weather.model.SortedByDateWeatherForecastResult
import com.rob.weather.model.WeatherForecastResult
import com.rob.weather.model.WeatherToday
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class GeneralDayTodayViewModel constructor(private val weatherForecastRepository: WeatherForecastRepository) : ViewModel() {
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    private val _sortedWeatherForecastResult =
        MutableLiveData<List<SortedByDateWeatherForecastResult>>()
    val sortedWeatherForecastResult: LiveData<List<SortedByDateWeatherForecastResult>> =
        _sortedWeatherForecastResult
    private val _weatherToday = MutableLiveData<WeatherToday>()
    val weatherToday: LiveData<WeatherToday> = _weatherToday

    fun getAllWeatherForecast(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherForecastResult = weatherForecastRepository.getWeatherForecast(city)
            if (weatherForecastResult != null) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = R.string.error.toString()
                    getWeatherToday(weatherForecastResult)
                    getWithoutFirstElementSortedByDateForecastResponseList(weatherForecastResult)
                }
            } else {
                _errorMessage.value = R.string.error.toString()
            }
        }
    }

    fun getWeatherToday(weatherForecastResult: WeatherForecastResult) {
        val date = " Сегодня, " + weatherForecastResult.list.first().date.changeDateFormat()
        val cityName: String = weatherForecastResult.city.name.toString()
        val temperature: String =
            weatherForecastResult.list.first().main.temp.toInt()
                .toString() + "°"
        val description: String =
            weatherForecastResult.list.first().weather.first().description.toString()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } + ", ощущается как  " + weatherForecastResult.list.first().main.temp_max.toInt()
                .toString() + "°"
        val iconCode = weatherForecastResult.list.first().weather.first().icon

        val todayWeather =
            WeatherToday(date, cityName, temperature, description, iconCode)
        _weatherToday.value = todayWeather
        _weatherToday.value!!.city = _weatherToday.value!!.city.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
    }

    private fun geWeatherForecastResponseGroupByDate(weatherForecastResult: WeatherForecastResult): List<SortedByDateWeatherForecastResult> {
        val weatherForecastGroup =
            weatherForecastResult.list.groupBy { (it.date).changeDateFormat() }
        return (weatherForecastGroup.keys).map { date ->
            val forecasts = weatherForecastGroup[date] ?: emptyList()
            SortedByDateWeatherForecastResult(date, forecasts)
        }
    }

    private fun getWithoutFirstElementSortedByDateForecastResponseList(weatherForecastResult: WeatherForecastResult) {
        val sortedByDateForecastResponseList =
            geWeatherForecastResponseGroupByDate(weatherForecastResult)
        val withoutFirstElementSortedByDateForecastResponseList =
            sortedByDateForecastResponseList.toMutableList()
                .subList(1, sortedByDateForecastResponseList.size)
        _sortedWeatherForecastResult.postValue(
            withoutFirstElementSortedByDateForecastResponseList
        )
    }
}





