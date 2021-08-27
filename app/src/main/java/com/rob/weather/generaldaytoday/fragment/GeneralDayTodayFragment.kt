package com.rob.weather.generaldaytoday.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rob.weather.R
import com.rob.weather.utils.BaseFragment
import com.rob.weather.utils.Utils.id_key
import com.rob.weather.utils.Utils.city
import com.rob.weather.databinding.FragmentGeneralDayTodayBinding
import com.rob.weather.generaldaytoday.retrofit.RemoteDataSource
import com.rob.weather.generaldaytoday.adapters.GeneralDayTodayAdapter
import com.rob.weather.generaldaytoday.repository.WeatherForecastRepository
import com.rob.weather.generaldaytoday.viewmodel.GeneralDayTodayViewModel
import com.squareup.picasso.Picasso

class GeneralDayTodayFragment :
    BaseFragment<FragmentGeneralDayTodayBinding>(FragmentGeneralDayTodayBinding::inflate) {
    private val viewModel: GeneralDayTodayViewModel by viewModels {
        MyViewModelFactory(WeatherForecastRepository(id_key, RemoteDataSource.RetrofitServices.getClient("https://api.openweathermap.org/")))
    }
    private val dialog = ShowDialogForChangingCity()

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllWeatherForecast(city)
        val allDaysWeatherListAdapter = GeneralDayTodayAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.adapter = allDaysWeatherListAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel.sortedWeatherForecastResult.observe(viewLifecycleOwner) { list ->
            allDaysWeatherListAdapter.setData(list)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            binding.currentTemperatureTextView.text = error
        }

        viewModel.weatherToday.observe(viewLifecycleOwner) {
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
                    dialog.showDialog(requireContext(), viewModel)
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

