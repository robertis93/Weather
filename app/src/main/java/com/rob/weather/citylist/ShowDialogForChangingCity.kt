package com.rob.weather.citylist

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.rob.weather.citylist.viewmodel.CityListViewModel
import com.rob.weather.citylist.viewmodel.DialogCityAdapter
import com.rob.weather.databinding.SearchCityDialogBinding
import com.rob.weather.utils.Utils.city
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class ShowDialogForChangingCity {
    private var cityName: ArrayList<String> = ArrayList()
    fun showDialog(context: Context, viewModel: CityListViewModel) {
        val builder = AlertDialog.Builder(context)
        val layoutInflater = LayoutInflater.from(context)
        val dialogBinding = SearchCityDialogBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)
        val alertDialog = builder.show()
        val obj = JSONObject(loadJSONFromAsset(context))
        val cityArray = obj.getJSONArray("cities")
        (0 until cityArray.length())
            .asSequence()
            .map { cityArray.getJSONObject(it) }
            .map { (it.getString("name")) }
            .forEach { cityName.add(it) }

        val cityListRecyclerView = dialogBinding.recyclerView
        val cityClickListener: DialogCityAdapter.OnCityClickListener =
            object : DialogCityAdapter.OnCityClickListener {
                override fun onCityClick(cityName: String, position: Int) {
                    dialogBinding.searchCityEditText.setText(cityName)
                    cityListRecyclerView.isVisible = false
                }
            }

        val cityAdapter = DialogCityAdapter(cityClickListener)
        cityListRecyclerView.adapter = cityAdapter
        cityListRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        cityAdapter.setData(cityName)
        dialogBinding.addBtn.setOnClickListener {
            city = dialogBinding.searchCityEditText.text.toString()
            val city = dialogBinding.searchCityEditText.text.toString()
            viewModel.addCity(city)
            alertDialog.dismiss()
        }

        dialogBinding.searchCityEditText.doAfterTextChanged { nameCity ->
            val city = nameCity.toString()
            val z = cityName.filter { it.startsWith(city, ignoreCase = true) }
            cityAdapter.setData(z)
            if (nameCity.toString().length < 3) {
                cityListRecyclerView.isVisible = false
            }

            if (nameCity.toString().length > 2) {
                cityListRecyclerView.isVisible = true

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