package com.rob.weather.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rob.weather.R
import com.rob.weather.databinding.FragmentGeneralDayTodayBinding
import com.rob.weather.databinding.SearchCityDialogBinding
import com.rob.weather.view.adapters.GeneralDayTodayAdapter
import com.rob.weather.viewmodel.Retrofit.RetrofitServices
import com.rob.weather.viewmodel.repository.Repository
import com.rob.weather.viewmodel.viewmodels.GeneralDayTodayViewModel
import com.squareup.picasso.Picasso
import java.util.*

class GeneralDayTodayFragment :
    BaseFragment<FragmentGeneralDayTodayBinding>(FragmentGeneralDayTodayBinding::inflate) {
    lateinit var viewModel: GeneralDayTodayViewModel
    private var city = "Тамбов"

    companion object {
        var AppId = "2e65127e909e178d0af311a81f39948c"
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            GeneralDayTodayViewModel.MyViewModelFactory(Repository(RetrofitServices.getClient("https://api.openweathermap.org/")))
        ).get(GeneralDayTodayViewModel::class.java)

        val allDaysWeatherListAdapter = GeneralDayTodayAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.adapter = allDaysWeatherListAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel.sortedWeatherForecastResult.observe(viewLifecycleOwner) { list ->
            allDaysWeatherListAdapter.setData(list)
        }

        viewModel.weatherTodayLiveData.observe(viewLifecycleOwner) { it ->
            with(binding) {
                currentDateTextView.text = it.date
                currentTemperatureTextView.text = it.temperature
                currentWeatherDescriptionTextView.text = it.description
                toolbarToday.text = it.city.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
                val iconCode = it.icon
                val iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png"
                Picasso.get().load(iconUrl).into(weatherIcon)
            }
        }

        viewModel.getAllWeatherForecast(city, AppId)
        val toolbar = binding.toolbar

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_search -> {
                    changeCity()
                    true
                }
                R.id.action_loader -> {
                    // TODO : action
                    true
                }
                else -> onOptionsItemSelected(it)
            }
        }

        binding.blueRectangleView.setOnClickListener {
            findNavController().navigate(R.id.action_weatherInformationByDayFragment_to_chooseDayFragment)
        }
    }

    private fun changeCity() {
        val builder = AlertDialog.Builder(requireContext())
        val layoutInflater = LayoutInflater.from(requireContext())
        val dialogFragment = SearchCityDialogBinding.inflate(layoutInflater)
        builder.setView(dialogFragment.root)

        val alertDialog = builder.show()

        dialogFragment.addBtn.setOnClickListener {
            city = dialogFragment.searchCityEditText.text.toString()
            viewModel.getAllWeatherForecast(city, AppId)
            alertDialog.dismiss()
        }
        dialogFragment.crossImageBtn.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogFragment.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
    }

//    private fun showOption(id: Int) {
//        val item: MenuItem? = menu?.findItem(id)
//        if (item != null) {
//            item.isVisible = false
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}

