package com.rob.weather.selectedday

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rob.weather.utils.Utils.timeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class SelectedDayViewModel() : ViewModel() {
    private val _isSunRise = MutableSharedFlow<Unit>()
    val isSunRise: SharedFlow<Unit> = _isSunRise.asSharedFlow()

    private val _isDay = MutableSharedFlow<Unit>()
    val isDay: SharedFlow<Unit> = _isDay.asSharedFlow()

    private val _isNight = MutableSharedFlow<Unit>()
    val isNight: SharedFlow<Unit> = _isNight.asSharedFlow()

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
}

fun timestampToDisplayTime(dayTimeStamp: Long): String {
    val currentDate = Date(dayTimeStamp)
    return timeFormat.format(currentDate)
}





