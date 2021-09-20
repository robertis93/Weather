package com.rob.weather.generaldaytoday.fragment

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.rob.weather.citylist.CityAdapter
import com.rob.weather.citylist.viewmodel.CityListViewModel
import com.rob.weather.citylist.viewmodel.DialogCityAdapter
import com.rob.weather.databinding.SearchCityDialogBinding
import com.rob.weather.utils.Utils
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class ShowDialogForChangingCity {
    var cityName: ArrayList<String> = ArrayList()
    fun showDialog(context: Context, viewModel: CityListViewModel) {
        val builder = AlertDialog.Builder(context)
        val layoutInflater = LayoutInflater.from(context)
        val dialogBinding = SearchCityDialogBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)
        val alertDialog = builder.show()
        val obj = JSONObject(loadJSONFromAsset(context))
        val userArray = obj.getJSONArray("cities")
        for (i in 0 until userArray.length()) {
            val userDetail = userArray.getJSONObject(i)
            val id = (userDetail.getString("city_id"))
            val name = (userDetail.getString("name"))
            val state = (userDetail.getString("region_id"))
            val country = (userDetail.getString("country_id"))
            // val city = City(id, country, name, state)

            cityName.add(name)
        }


        val measureRecyclerView = dialogBinding.recyclerView
        val alarmMeasureDeleteClickListener: DialogCityAdapter.OnAlarmClickListener =
            object : DialogCityAdapter.OnAlarmClickListener {
                override fun onDeleteAlarmClick(cityName: String, position: Int) {
                    dialogBinding.searchCityEditText.setText(cityName)
                    Log.i("myLogs", "recyclerClicked")
                    measureRecyclerView.isVisible = false
                }
            }

        val cityAdapter = DialogCityAdapter(alarmMeasureDeleteClickListener)
        measureRecyclerView.adapter = cityAdapter
        measureRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        cityAdapter.setData(cityName)
        dialogBinding.addBtn.setOnClickListener {
            Utils.city = dialogBinding.searchCityEditText.text.toString()
            val city = dialogBinding.searchCityEditText.text.toString()
            viewModel.addCity(city)
            //   viewModel.getAllWeatherForecast(Utils.city)
            alertDialog.dismiss()
        }

        dialogBinding.searchCityEditText.doAfterTextChanged { nameCity ->

            val city = nameCity.toString()
            val z = cityName.filter { it.startsWith(city, ignoreCase = true) }
            cityAdapter.setData(z)
            if (nameCity.toString().length < 3) {
                measureRecyclerView.isVisible = false

            }

            if (nameCity.toString().length > 2) {
                measureRecyclerView.isVisible = true

            }
        }
        dialogBinding.crossImageBtn.setOnClickListener {
            alertDialog.dismiss()
        }
        dialogBinding.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}

private fun loadJSONFromAsset(contex: Context): String {
    val json: String?
    try {
        val inputStream = contex.assets.open("cities.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        val charset: Charset = Charsets.UTF_8
        inputStream.read(buffer)
        inputStream.close()
        json = String(buffer, charset)
    } catch (ex: IOException) {
        ex.printStackTrace()
        return ""
    }
    return json
}