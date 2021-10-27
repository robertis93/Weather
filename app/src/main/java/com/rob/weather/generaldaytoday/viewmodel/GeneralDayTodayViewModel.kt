package com.rob.weather.generaldaytoday.viewmodel

import android.view.MenuItem
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rob.weather.R
import com.rob.weather.datasource.retrofit.WeatherDataFromRemoteSource
import com.rob.weather.model.SortedByDateWeatherForecastResult
import com.rob.weather.model.WeatherForecastResult
import com.rob.weather.model.WeatherToday
import com.rob.weather.utils.Utils.fullDateFormat
import com.rob.weather.utils.Utils.shortDateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class GeneralDayTodayViewModel(val dataSource: WeatherDataFromRemoteSource) : ViewModel() {
    private val _errorMessage = MutableStateFlow<Int>(R.string.empty)
    val errorMessage: StateFlow<Int> = _errorMessage.asStateFlow()
    private val _sortedWeatherForecastResult =
        MutableSharedFlow<List<SortedByDateWeatherForecastResult>>()
    val sortedWeatherForecastResult: SharedFlow<List<SortedByDateWeatherForecastResult>> =
        _sortedWeatherForecastResult.asSharedFlow()
    private val _fullInfoTodayWeather =
        MutableSharedFlow<SortedByDateWeatherForecastResult>()
    val fullInfoTodayWeather: SharedFlow<SortedByDateWeatherForecastResult> =
        _fullInfoTodayWeather.asSharedFlow()
    private val _searchingCity = MutableSharedFlow<Unit>(
        replay = 0, extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val searchingCity: SharedFlow<Unit> = _searchingCity.asSharedFlow()
    private val _changingMode = MutableSharedFlow<Unit>()
    val changingMode: SharedFlow<Unit> = _changingMode.asSharedFlow()
    private val _weatherToday = MutableSharedFlow<WeatherToday>()
    val weatherToday: SharedFlow<WeatherToday> = _weatherToday.asSharedFlow()
    private var _progressBar = MutableStateFlow<Boolean>(true)
    val progressBar: StateFlow<Boolean> = _progressBar.asStateFlow()
    private var _updatingInformation = MutableStateFlow<Boolean>(false)
    val updatingInformation: StateFlow<Boolean> = _updatingInformation.asStateFlow()

    fun getAllWeatherForecast(city: String?) {
        if (city != null) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val weatherForecast = dataSource.getWeatherForecastResponse(city)
                    if (weatherForecast != null) {
                        withContext(Dispatchers.Main) {
                            _updatingInformation.value = false
                            setWeatherToday(weatherForecast)
                            withoutFirstElementSortedByDateForecastResponseList(weatherForecast)
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
    }

    private suspend fun setWeatherToday(weatherForecast: WeatherForecastResult) {
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
        _progressBar.value = false
        _weatherToday.emit(todayWeather)
    }

    private suspend fun withoutFirstElementSortedByDateForecastResponseList(
        weatherForecast: WeatherForecastResult
    ) {
        val sortedByDateForecastResponseList =
            geWeatherForecastResponseGroupByDate(weatherForecast)
        val withoutFirstElementSortedByDateForecastResponseList =
            sortedByDateForecastResponseList
                .subList(1, sortedByDateForecastResponseList.size)
        _sortedWeatherForecastResult.emit(withoutFirstElementSortedByDateForecastResponseList)
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

    fun getMoreInformationToday(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val weatherForecast = dataSource.getWeatherForecastResponse(city)
                withContext(Dispatchers.Main) {
                    val todayWeather = getFullWeatherTodayResponse(weatherForecast)
                    _fullInfoTodayWeather.emit(todayWeather)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = R.string.error_server
                    _progressBar.value = false
                }
            }
        }
    }

    private fun getFullWeatherTodayResponse(weatherForecast: WeatherForecastResult)
            : SortedByDateWeatherForecastResult {
        return geWeatherForecastResponseGroupByDate(weatherForecast)[0]
    }

    fun updateInformation(city: String) {
        getAllWeatherForecast(city)
        _updatingInformation.value = true
    }

    fun clickOnMenu(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_search -> {
                searchCity()
                true
            }
            R.id.switch_mode -> {
                changeMode()
                true
            }
        }
        return true
    }

    fun searchCity(): Boolean {
        viewModelScope.launch(Dispatchers.IO) {
            _searchingCity.emit(Unit)
        }
        return true
    }

    fun changeMode(): Boolean {
        viewModelScope.launch(Dispatchers.IO) {
            _changingMode.emit(Unit)
        }
        return true
    }
}

private fun String.changeDateFormat(): String {
    val changedDate = fullDateFormat.parse(this)
    return shortDateFormat.format(changedDate)
}





