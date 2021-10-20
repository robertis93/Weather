package com.rob.weather.generaldaytoday.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.rob.weather.R
import com.rob.weather.databinding.FragmentGeneralDayTodayBinding
import com.rob.weather.generaldaytoday.adapters.GeneralDayTodayAdapter
import com.rob.weather.generaldaytoday.viewmodel.GeneralDayTodayViewModel
import com.rob.weather.model.WeatherToday
import com.rob.weather.utils.BaseFragment
import com.rob.weather.utils.Utils.BASE_URL_IMAGE
import com.rob.weather.utils.Utils.city
import com.rob.weather.utils.extensions.getAppComponent
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collect

class GeneralDayTodayFragment :
    BaseFragment<FragmentGeneralDayTodayBinding>(FragmentGeneralDayTodayBinding::inflate) {
    private lateinit var generalDayTodayViewModelFactory: GeneralDayTodayViewModelFactory
    private val generalDayTodayViewModel: GeneralDayTodayViewModel by viewModels { generalDayTodayViewModelFactory }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        generalDayTodayViewModelFactory = getAppComponent().getDependencyGeneralDay()
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val allDaysWeatherListAdapter = GeneralDayTodayAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.adapter = allDaysWeatherListAdapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            generalDayTodayViewModel.sortedWeatherForecastResult.collect { list ->
                allDaysWeatherListAdapter.setData(list)
            }
        }

        binding.blueRectangleView.setOnClickListener {
            generalDayTodayViewModel.getMoreInformationToday(city)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            generalDayTodayViewModel.currentWeather.collect { currentWeather ->
                val action =
                    GeneralDayTodayFragmentDirections
                        .actionWeatherInformationByDayFragmentToChooseDayFragment3(
                            currentWeather
                        )
                findNavController().navigate(action)
            }
        }

        lifecycleScope.launchWhenStarted {
            generalDayTodayViewModel.errorMessage
                .collect { error ->
                    binding.currentWeatherDescriptionTextview.text = getString(error)
                }
        }

        lifecycleScope.launchWhenStarted {
            generalDayTodayViewModel.weatherToday
                .collect { initializingScreenForToday(it) }
        }

        lifecycleScope.launchWhenStarted {
            generalDayTodayViewModel.progressBar
                .collect { visible ->
                    binding.progressBar.isVisible = visible
                }
        }

        val toolbar = binding.toolbar
        toolbar.setOnMenuItemClickListener { menuItem ->
            generalDayTodayViewModel.clickOnMenu(menuItem)
        }

        lifecycleScope.launchWhenStarted {
            generalDayTodayViewModel.searchingCity
                .collect {
                    findNavController()
                        .navigate(R.id.action_weatherInformationByDayFragment_to_cityListFragment)
                }
        }

        lifecycleScope.launchWhenStarted {
            generalDayTodayViewModel.changingMode
                .collect {
                }
        }

        binding.swipeRefresh.setOnRefreshListener(OnRefreshListener {
            generalDayTodayViewModel.updateInformation(city)
        })

        lifecycleScope.launchWhenStarted {
            generalDayTodayViewModel.updatingInformation
                .collect { visible ->
                    binding.swipeRefresh.isRefreshing = visible
                }
        }

        generalDayTodayViewModel.getAllWeatherForecast(city)
    }

    private fun initializingScreenForToday(weatherToday: WeatherToday) {
        with(binding) {
            currentDateTextView.text =
                requireContext().getString(R.string.today_with_comma) + weatherToday.date
            currentTemperatureTextview.text = weatherToday.temperature +
                    requireContext().getString(R.string.celsius_icon)
            currentWeatherDescriptionTextview.text =
                weatherToday.description + requireContext().getString(R.string.feels_like) +
                        weatherToday.temperature + requireContext().getString(
                    R.string.celsius_icon
                )
            toolbarToday.text = weatherToday.city
            val iconCode = weatherToday.icon
            val iconUrl = BASE_URL_IMAGE + iconCode + ".png"
            binding.weatherIcon.visibility = View.VISIBLE
            Picasso.get().load(iconUrl).into(weatherIcon)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}

