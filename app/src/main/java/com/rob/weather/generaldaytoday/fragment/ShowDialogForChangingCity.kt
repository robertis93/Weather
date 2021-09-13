package com.rob.weather.generaldaytoday.fragment

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.rob.weather.citylist.fragment.CityListFragment
import com.rob.weather.citylist.viewmodel.CityListViewModel
import com.rob.weather.utils.Utils
import com.rob.weather.databinding.SearchCityDialogBinding
import com.rob.weather.generaldaytoday.viewmodel.GeneralDayTodayViewModel
import javax.inject.Inject
import javax.inject.Singleton

class ShowDialogForChangingCity {
    fun showDialog(context: Context, viewModel: CityListViewModel) {
        val builder = AlertDialog.Builder(context)
        val layoutInflater = LayoutInflater.from(context)
        val dialogFragment = SearchCityDialogBinding.inflate(layoutInflater)
        builder.setView(dialogFragment.root)
        val alertDialog = builder.show()
        dialogFragment.addBtn.setOnClickListener {
          //  Utils.city = dialogFragment.searchCityEditText.text.toString()
            val city =  dialogFragment.searchCityEditText.text.toString()
            viewModel.addCity(city)
           // viewModel.getAllWeatherForecast(Utils.city)
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