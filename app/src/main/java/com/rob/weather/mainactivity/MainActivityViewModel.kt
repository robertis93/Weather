package com.rob.weather.mainactivity

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rob.weather.App
import com.rob.weather.R
import com.rob.weather.citylist.database.WeatherRepository
import com.rob.weather.datasource.retrofit.WeatherDataFromRemoteSource
import com.rob.weather.generaldaytoday.model.WeatherForecastForNextDays
import com.rob.weather.model.WeatherForecastResult
import com.rob.weather.model.WeatherToday
import com.rob.weather.utils.Utils.fullDateFormat
import com.rob.weather.utils.Utils.shortDateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivityViewModel(
    val dataSource: WeatherDataFromRemoteSource,
    private val repository: WeatherRepository,
    private val app: App
) : ViewModel() {
    private var fusedLocationClient: FusedLocationProviderClient
    private val _errorMessage = MutableStateFlow<Int>(R.string.empty)
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
        MutableSharedFlow<WeatherForecastForNextDays>()
    val fullInfoTodayWeather: SharedFlow<WeatherForecastForNextDays> =
        _fullInfoTodayWeather.asSharedFlow()

    private val _searchingCity = MutableSharedFlow<Unit>(
        replay = 0
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

    init {
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(app.applicationContext)
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

    fun checkDataBase() {
        viewModelScope.launch(Dispatchers.IO)
        {
            withContext(Dispatchers.Main) {
                fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    var location: Location? = task.result
                    val currentLatitude = location?.latitude
                    val currentLongitude = location?.longitude
                    if (currentLatitude != null && currentLongitude != null) {
                        getWeatherInCity(currentLatitude, currentLongitude)
                    }
                }
            }
        }
    }

    private fun getWeatherInCity(currentLatitude: Double, currentLongitude: Double) {
        viewModelScope.launch(Dispatchers.Main) {
            val weatherForecastResult = repository
                .getWeatherInCityByCoordinates(currentLatitude, currentLongitude)
            val cityName = weatherForecastResult.city.name
            val city = com.rob.weather.citylist.model.City(cityName)
            repository.insert(city)
            //  getAllWeatherForecast(cityName)
        }
    }

    private fun String.changeDateFormat(): String {
        val changedDate = fullDateFormat.parse(this)
        return shortDateFormat.format(changedDate)
    }
}





