package com.rob.weather.generaldaytoday.viewmodel

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rob.weather.App
import com.rob.weather.R
import com.rob.weather.citylist.database.WeatherRepository
import com.rob.weather.datasource.retrofit.WeatherDataFromRemoteSource
import com.rob.weather.generaldaytoday.model.WeatherForecastForNextDays
import com.rob.weather.model.SortedByDateWeatherForecastResult
import com.rob.weather.model.WeatherForecastResult
import com.rob.weather.model.WeatherToday
import com.rob.weather.utils.Utils.fullDateFormat
import com.rob.weather.utils.Utils.shortDateFormat
import com.rob.weather.utils.Utils.timeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class GeneralDayTodayViewModel(
    val dataSource: WeatherDataFromRemoteSource,
    private val repository: WeatherRepository,
    private val app: App
) : ViewModel() {
    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(app.applicationContext)
    private val _errorMessage = MutableStateFlow(R.string.empty)
    val errorMessage: StateFlow<Int> = _errorMessage.asStateFlow()

    private val _weatherForNextDays =
        MutableSharedFlow<List<WeatherForecastForNextDays>>()
    val weatherForNextDays: SharedFlow<List<WeatherForecastForNextDays>> =
        _weatherForNextDays.asSharedFlow()

    private val _cityName =
        MutableSharedFlow<String>()
    val cityName: SharedFlow<String> =
        _cityName.asSharedFlow()

    private val _city =
        MutableSharedFlow<String>()
    val city: SharedFlow<String> =
        _city.asSharedFlow()

    private val _fullInfoTodayWeather =
        MutableSharedFlow<WeatherForecastForNextDays>(
            replay = 0, extraBufferCapacity = 10,
            BufferOverflow.SUSPEND
        )
    val fullInfoTodayWeather: SharedFlow<WeatherForecastForNextDays> =
        _fullInfoTodayWeather.asSharedFlow()

    private val _searchingCity = MutableSharedFlow<Unit>(
        replay = 0
    )
    val searchingCity: SharedFlow<Unit> = _searchingCity.asSharedFlow()

    private val _weatherToday = MutableSharedFlow<WeatherToday>()
    val weatherToday: SharedFlow<WeatherToday> = _weatherToday.asSharedFlow()

    private var _progressBar = MutableStateFlow(true)
    val progressBar: StateFlow<Boolean> = _progressBar.asStateFlow()

    private val _isSunRise = MutableSharedFlow<Unit>()
    val isSunRise: SharedFlow<Unit> = _isSunRise.asSharedFlow()

    private val _isDay = MutableSharedFlow<Unit>()
    val isDay: SharedFlow<Unit> = _isDay.asSharedFlow()

    private val _isNight = MutableSharedFlow<Unit>()
    val isNight: SharedFlow<Unit> = _isNight.asSharedFlow()

    private var _updatingInformation = MutableStateFlow(false)
    val updatingInformation: StateFlow<Boolean> = _updatingInformation.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.N)
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun convertToWeatherForNextDays(weatherForNextDays: List<SortedByDateWeatherForecastResult>)
            : List<WeatherForecastForNextDays> {
        val weatherForecastForNextDayList = mutableListOf<WeatherForecastForNextDays>()
        for (weatherForOneDay in weatherForNextDays) {
            weatherForecastForNextDayList.add(convertToWeatherForecastForNextDays(weatherForOneDay))
        }
        return weatherForecastForNextDayList
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun convertToWeatherForecastForNextDays(weatherForOneDay: SortedByDateWeatherForecastResult):
            WeatherForecastForNextDays {
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
        val humidity = weatherForOneDay.forecastResponseList.first().main.humidity.toString()
        val averageTemperature =
            weatherForOneDay.forecastResponseList.first().main.temp.toInt().toString()
        val windSpeed =
            weatherForOneDay.forecastResponseList.first().wind.speed.toInt().toString()
        val preciptation = weatherForOneDay.forecastResponseList.first().clouds.all.toString()
        val descriptionWeather =
            weatherForOneDay.forecastResponseList.first().weather.first().description
                .replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                    else "$it"
                }
        val weatherForecastForNextDays = WeatherForecastForNextDays(
            date,
            city,
            weekDay,
            minTemperatureForDay,
            maxTemperatureForDay,
            humidity,
            averageTemperature,
            windSpeed,
            preciptation,
            descriptionWeather,
            iconCode,
            timeAndTemperatureList
        )
        return weatherForecastForNextDays
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

    @RequiresApi(Build.VERSION_CODES.N)
    fun getMoreInformationToday(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val weatherForecast = dataSource.getWeatherForecastResponse(city)
                withContext(Dispatchers.Main) {
                    val todayWeather = getFullWeatherTodayResponse(weatherForecast)
                    _fullInfoTodayWeather
                        .emit(
                            convertToWeatherForNextDays(listOf(todayWeather))
                                .first()
                        )
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = R.string.error_server
                    _progressBar.value = false
                }
            }
        }
    }

    private fun getFullWeatherTodayResponse(weatherForecast: WeatherForecastResult):
            SortedByDateWeatherForecastResult {
        return getWeatherSortedByDate(weatherForecast)[0]
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun updateInformation(city: String) {
        getAllWeatherForecast(city)
        _updatingInformation.value = true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun updateWeatherForecastInformation() {
        _updatingInformation.value = true
        checkDataBase()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun checkDataBase() {
        viewModelScope.launch(Dispatchers.IO)
        {
            withContext(Dispatchers.Main) {
                val cityInDataBase = repository.getAllCities()
                val citySizeFromDB = cityInDataBase.size
                if (citySizeFromDB == 0) {
                    if (ActivityCompat.checkSelfPermission(
                            app.applicationContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            app.applicationContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return@withContext
                    }
                    fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                        var location: Location? = task.result
                        if (location == null) {
                            viewModelScope.launch(Dispatchers.IO) {
                                val cityName = repository.getIP().city
                                getAllWeatherForecast(cityName)
                            }
                        } else {
                            val currentLatitude = location.latitude
                            val currentLongitude = location.longitude
                            getWeatherInCity(currentLatitude, currentLongitude)
                        }
                    }

                } else {
                    val lastCityInDataBase = cityInDataBase.first()
                    getAllWeatherForecast(lastCityInDataBase.name)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getWeatherInCity(currentLatitude: Double, currentLongitude: Double) {
        viewModelScope.launch(Dispatchers.Main) {
            val weatherForecastResult = repository
                .getWeatherInCityByCoordinates(currentLatitude, currentLongitude)
            val cityName = weatherForecastResult.city.name
            getAllWeatherForecast(cityName)
        }
    }

    fun getCityFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            _cityName.emit(repository.getAllCities().first().name)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getCityByGeolocation() {
        viewModelScope.launch(Dispatchers.IO)
        {
            withContext(Dispatchers.Main) {
                val cityInDataBase = repository.getAllCities()
                val citySizeFromDB = cityInDataBase.size
                val lastCityInDataBase = cityInDataBase.first()
                getAllWeatherForecast(lastCityInDataBase.name)
            }
        }
    }

    fun checkTime() {
        val dateCalendar: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT"))
        val dateTimeStamp = dateCalendar.time.time
        val currentTime = timestampToDisplayTime(dateTimeStamp)
        val hour = currentTime.substringBefore(":").trim().toInt()
        val minute = currentTime.substringAfter(":").trim().toInt()
        val numberOfMinutes = hour * 60 + minute

        if (numberOfMinutes in 360..480 || numberOfMinutes in 1260..1380
        ) {
            viewModelScope.launch(Dispatchers.Main) {
                _isSunRise.emit(Unit)
            }
        } else if (numberOfMinutes in 480..1260) {
            viewModelScope.launch(Dispatchers.Main) {
                _isDay.emit(Unit)
            }
        } else {
            viewModelScope.launch(Dispatchers.Main) {
                _isNight.emit(Unit)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun checkArguments(arguments: Bundle?) {
        val text = arguments?.getString("MyArg")
        if (text != null) {
            getCityByGeolocation()
        }
    }
}

fun timestampToDisplayTime(dayTimeStamp: Long): String {
    val currentDate = Date(dayTimeStamp)
    return timeFormat.format(currentDate)
}

private fun String.changeDateFormat(): String {
    val changedDate = fullDateFormat.parse(this)
    return shortDateFormat.format(changedDate)
}





