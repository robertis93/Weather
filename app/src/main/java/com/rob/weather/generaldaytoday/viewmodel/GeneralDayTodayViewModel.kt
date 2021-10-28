package com.rob.weather.generaldaytoday.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rob.weather.R
import com.rob.weather.datasource.retrofit.WeatherDataFromRemoteSource
import com.rob.weather.generaldaytoday.model.WeatherForecastForNextDays
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

    private val _weatherForNextDays =
        MutableSharedFlow<List<WeatherForecastForNextDays>>()
    val weatherForNextDays: SharedFlow<List<WeatherForecastForNextDays>> =
        _weatherForNextDays.asSharedFlow()

    private val _fullInfoTodayWeather =
        MutableSharedFlow<WeatherForecastForNextDays>()
    val fullInfoTodayWeather: SharedFlow<WeatherForecastForNextDays> =
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
                            _weatherToday.emit(getWeatherToday(weatherForecast))
                            _weatherForNextDays
                                .emit(
                                    convertToWeatherForNextDays(
                                        getWeatherForNextDays(
                                            weatherForecast
                                        )
                                    )
                                )
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

    private fun getWeatherToday(weatherForecast: WeatherForecastResult): WeatherToday {
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
        return todayWeather
    }

    private fun convertToWeatherForNextDays(weatherForNextDays: List<SortedByDateWeatherForecastResult>)
            : List<WeatherForecastForNextDays> {
        val weatherForecastForNextDayList = mutableListOf<WeatherForecastForNextDays>()
        for (weatherForOneDay in weatherForNextDays) {
            weatherForOneDay
            val date = weatherForOneDay.date.substringBefore(",") + ","
            val city = weatherForOneDay.city
            val weekDay = weatherForOneDay.date.substringAfter(",")
            val minTemperatureForDay =
                weatherForOneDay.forecastResponseList
                    .stream()
                    .min { o1, o2 ->
                        compareValues((o1.main.temp_min), (o2.main.temp_min))
                    }
                    .map { it.main.temp_min }
                    .get().toInt().toString() + "°"
            val maxTemperatureForDay =
                weatherForOneDay.forecastResponseList
                    .stream()
                    .max { o1, o2 ->
                        compareValues((o1.main.temp_min), (o2.main.temp_min))
                    }
                    .map { it.main.temp_max }
                    .get().toInt().toString() + "°"
            val iconCode = weatherForOneDay.forecastResponseList.first().weather.first().icon
            val timeAndTemperatureList = weatherForOneDay.forecastResponseList
            val weatherForecastForNextDays = WeatherForecastForNextDays(
                date,
                city,
                weekDay,
                minTemperatureForDay,
                maxTemperatureForDay,
                iconCode,
                timeAndTemperatureList
            )
            weatherForecastForNextDayList.add(weatherForecastForNextDays)
        }
        return weatherForecastForNextDayList
    }

    private fun getWeatherForNextDays(
        weatherForecast: WeatherForecastResult
    ): List<SortedByDateWeatherForecastResult> {
        val weatherSortedByDate =
            getWeatherSortedByDate(weatherForecast)
        val weatherSortedByDateForNextDays =
            weatherSortedByDate
                .subList(1, weatherSortedByDate.size)
        return weatherSortedByDateForNextDays
    }

    private fun getWeatherSortedByDate(
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
                    _fullInfoTodayWeather.emit(convertToWeatherForNextDays(listOf(todayWeather)).first())
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
        return getWeatherSortedByDate(weatherForecast)[0]
    }

    fun updateInformation(city: String) {
        getAllWeatherForecast(city)
        _updatingInformation.value = true
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





