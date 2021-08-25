package com.rob.weather.generalDayToday.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rob.weather.R
import com.rob.weather.Utils.BaseFragment
import com.rob.weather.Utils.DateUtil.AppId
import com.rob.weather.Utils.DateUtil.city
import com.rob.weather.databinding.FragmentGeneralDayTodayBinding
import com.rob.weather.databinding.SearchCityDialogBinding
import com.rob.weather.generalDayToday.Retrofit.RetrofitServices
import com.rob.weather.generalDayToday.adapters.GeneralDayTodayAdapter
import com.rob.weather.generalDayToday.repository.Repository
import com.rob.weather.generalDayToday.viewmodel.GeneralDayTodayViewModel
import com.squareup.picasso.Picasso
import java.util.*

class GeneralDayTodayFragment :
    BaseFragment<FragmentGeneralDayTodayBinding>(FragmentGeneralDayTodayBinding::inflate) {
    private val viewModel: GeneralDayTodayViewModel by viewModels {
        GeneralDayTodayViewModel.MyViewModelFactory(Repository(RetrofitServices.getClient("https://api.openweathermap.org/")))
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val allDaysWeatherListAdapter = GeneralDayTodayAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.adapter = allDaysWeatherListAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel.sortedWeatherForecastResultLiveData.observe(viewLifecycleOwner) { list ->
            allDaysWeatherListAdapter.setData(list)
        }

        viewModel.errorMessageLiveData.observe(viewLifecycleOwner) { error ->
            binding.currentTemperatureTextView.text = error
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
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}

