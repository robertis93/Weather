package com.rob.weather.mainactivity

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(
) : ViewModel() {
    private val _bundle =
        MutableSharedFlow<Bundle>()
    val bundle: SharedFlow<Bundle> =
        _bundle.asSharedFlow()

    fun putToBundle() {
        viewModelScope.launch(Dispatchers.Main) {
            val bundle = Bundle()
            val city = "Bundle"
            bundle.putString("MyArg", city)
            _bundle.emit(bundle)
        }
    }
}





