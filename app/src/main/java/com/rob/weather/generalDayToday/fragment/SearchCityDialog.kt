package com.rob.weather.generalDayToday.fragment

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.rob.weather.Utils.Utils
import com.rob.weather.databinding.SearchCityDialogBinding
import com.rob.weather.generalDayToday.viewmodel.GeneralDayTodayViewModel

class SearchCityDialog() {
    fun changeCity(context: Context, viewModel: GeneralDayTodayViewModel) {
        val builder = AlertDialog.Builder(context)
        val layoutInflater = LayoutInflater.from(context)
        val dialogFragment = SearchCityDialogBinding.inflate(layoutInflater)
        builder.setView(dialogFragment.root)
        val alertDialog = builder.show()
        dialogFragment.addBtn.setOnClickListener {
            Utils.city = dialogFragment.searchCityEditText.text.toString()
            viewModel.getAllWeatherForecast(Utils.city)
            alertDialog.dismiss()
        }
        dialogFragment.crossImageBtn.setOnClickListener {
            alertDialog.dismiss()
        }
        dialogFragment.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}