package com.rob.weather.generalDayToday.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rob.weather.R
import com.rob.weather.Utils.Utils.changeDateFormat
import com.rob.weather.generalDayToday.repository.Repository
import com.rob.weather.model.SortedByDateWeatherForecastResult
import com.rob.weather.model.WeatherForecastResult
import com.rob.weather.model.WeatherToday
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class GeneralDayTodayViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val errorMessage = MutableLiveData<String>()
    val errorMessageLiveData: LiveData<String> = errorMessage
    private val sortedWeatherForecastResult =
        MutableLiveData<List<SortedByDateWeatherForecastResult>>()
    val sortedWeatherForecastResultLiveData: LiveData<List<SortedByDateWeatherForecastResult>> =
        sortedWeatherForecastResult
    private val weatherToday = MutableLiveData<WeatherToday>()
    val weatherTodayLiveData: LiveData<WeatherToday> = weatherToday

    fun getAllWeatherForecast(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherForecastResult = repository.getAll(city)
            if (weatherForecastResult != null) {
                withContext(Dispatchers.Main) {
                    errorMessage.value = R.string.error.toString()
                    getWeatherToday(weatherForecastResult)
                    getWithoutFirstElementSortedByDateForecastResponseList(weatherForecastResult)
                }
            } else {
                errorMessage.value = R.string.error.toString()
            }
        }
    }

    fun getWeatherToday(weatherForecastResult: WeatherForecastResult) {
        val date = "${" Сегодня, "}" + weatherForecastResult?.list?.first()?.date?.let {
            it.changeDateFormat()
        }
        val cityName: String = weatherForecastResult?.city?.name.toString()
        val temperature: String =
            weatherForecastResult?.list?.first()?.main?.temp?.toInt()
                .toString() + "${"°"}"
        val description: String =
            weatherForecastResult?.list?.first()?.weather?.first()?.description.toString()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } + "${", ощущается как  "}" + weatherForecastResult?.list?.first()?.main?.temp_max?.toInt()
                .toString() + "${"°"}"
        val iconCode = weatherForecastResult?.list?.first()?.weather?.first()?.icon

        val todayWeather =
            iconCode?.let {
                WeatherToday(date, cityName, temperature, description, it)
            }
        if (todayWeather != null) {
            weatherToday.value = todayWeather!!
            weatherToday.value!!.city = weatherToday.value!!.city.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        }
    }

    private fun geWeatherForecastResponseGroupByDate(weatherForecastResult: WeatherForecastResult): List<SortedByDateWeatherForecastResult> {
        val weatherForecastGroup =
            weatherForecastResult!!.list.groupBy { (it.date).changeDateFormat() }
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
        sortedWeatherForecastResult.postValue(
            withoutFirstElementSortedByDateForecastResponseList
        )
    }
}





