package com.rob.weather.generalDayToday.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rob.weather.R
import com.rob.weather.Utils.BaseFragment
import com.rob.weather.Utils.Utils.AppId
import com.rob.weather.Utils.Utils.city
import com.rob.weather.databinding.FragmentGeneralDayTodayBinding
import com.rob.weather.generalDayToday.Retrofit.RemoteDataSource
import com.rob.weather.generalDayToday.adapters.GeneralDayTodayAdapter
import com.rob.weather.generalDayToday.repository.Repository
import com.rob.weather.generalDayToday.viewmodel.GeneralDayTodayViewModel
import com.squareup.picasso.Picasso
import javax.inject.Inject

class GeneralDayTodayFragment :
    BaseFragment<FragmentGeneralDayTodayBinding>(FragmentGeneralDayTodayBinding::inflate) {
    private val viewModel: GeneralDayTodayViewModel by viewModels {
        MyViewModelFactory(Repository(AppId, RemoteDataSource.RetrofitServices.getClient("https://api.openweathermap.org/")))
    }
    private val dialog = SearchCityDialog()

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllWeatherForecast(city)
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

        viewModel.weatherTodayLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                currentDateTextView.text = it.date
                currentTemperatureTextView.text = it.temperature
                currentWeatherDescriptionTextView.text = it.description
                toolbarToday.text = it.city
                val iconCode = it.icon
                val iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png"
                Picasso.get().load(iconUrl).into(weatherIcon)
            }
        }

        val toolbar = binding.toolbar
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_search -> {
                    dialog.changeCity(requireContext(), viewModel)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}

