package com.rob.weather.generalDayToday.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.rob.weather.Utils.DateUtil
import com.rob.weather.generalDayToday.repository.Repository
import com.rob.weather.model.SortedByDateWeatherForecastResult
import com.rob.weather.model.WeatherForecastResult
import com.rob.weather.model.WeatherToday
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class GeneralDayTodayViewModel constructor(private val repository: Repository) : ViewModel() {

    private val errorMessage = MutableLiveData<String>()
    val errorMessageLiveData: LiveData<String> = errorMessage
    private val sortedWeatherForecastResult =
        MutableLiveData<List<SortedByDateWeatherForecastResult>>()
    val sortedWeatherForecastResultLiveData: LiveData<List<SortedByDateWeatherForecastResult>> =
        sortedWeatherForecastResult
    private val weatherToday = MutableLiveData<WeatherToday>()
    val weatherTodayLiveData: LiveData<WeatherToday> = weatherToday

    fun getAllWeatherForecast(city: String, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAll(city, id)?.enqueue(object : Callback<WeatherForecastResult> {
                @SuppressLint("SetTextI18n")
                override fun onFailure(call: Call<WeatherForecastResult>, t: Throwable) {
                    errorMessage.postValue(t.message)
                }

                override fun onResponse(
                    call: Call<WeatherForecastResult>,
                    response: Response<WeatherForecastResult>
                ) {

                    if (response.body() != null) {

                        val date = "${" Сегодня, "}" + response.body()?.list?.first()?.date?.let {
                            DateUtil.changeDateFormat(
                                it
                            )
                        }
                        val cityName: String = response.body()?.city?.name.toString()
                        val temperature: String =
                            response.body()?.list?.first()?.main?.temp?.toInt()
                                .toString() + "${"°"}"
                        val description: String =
                            response.body()?.list?.first()?.weather?.first()?.description.toString()
                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } + "${", ощущается как  "}" + response.body()?.list?.first()?.main?.temp_max?.toInt()
                                .toString() + "${"°"}"
                        val iconCode = response.body()?.list?.first()?.weather?.first()?.icon

                        val todayWeather =
                            iconCode?.let {
                                WeatherToday(
                                    date,
                                    cityName,
                                    temperature,
                                    description,
                                    it
                                )
                            }
                        if (todayWeather != null) {
                            weatherToday.value = todayWeather!!
                        }

                        fun geWeatherForecastResponseGroupByDate(): List<SortedByDateWeatherForecastResult> {
                            val weatherForecastGroup =
                                response.body()!!.list.groupBy { DateUtil.changeDateFormat(it.date) }
                            return (weatherForecastGroup.keys).map { date ->
                                val forecasts = weatherForecastGroup[date] ?: emptyList()
                                SortedByDateWeatherForecastResult(date, forecasts)
                            }
                        }

                        val sortedByDateForecastResponseList =
                            geWeatherForecastResponseGroupByDate()
                        val withoutFirstElementSortedByDateForecastResponseList =
                            sortedByDateForecastResponseList.toMutableList()
                                .subList(1, sortedByDateForecastResponseList.size)
                        sortedWeatherForecastResult.postValue(
                            withoutFirstElementSortedByDateForecastResponseList
                        )
                    }
                }
            }
            )
        }
    }

    class MyViewModelFactory constructor(private val repository: Repository) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(GeneralDayTodayViewModel::class.java)) {
                GeneralDayTodayViewModel(this.repository) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}




