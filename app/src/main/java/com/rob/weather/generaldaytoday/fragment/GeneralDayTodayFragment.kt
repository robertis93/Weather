package com.rob.weather.generaldaytoday.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
        generalDayTodayViewModel.sortedWeatherForecastResult.observe(viewLifecycleOwner) { list ->
            allDaysWeatherListAdapter.setData(list)
        }

        binding.blueRectangleView.setOnClickListener {
            generalDayTodayViewModel.firstSortedWeatherForecastResult.observe(viewLifecycleOwner) {
                lifecycleScope.launchWhenStarted {
                    generalDayTodayViewModel.getMoreInformationToday(city)
                    generalDayTodayViewModel.currentWeather
                        .collect { currentWeather ->
                            val action =
                                GeneralDayTodayFragmentDirections
                                    .actionWeatherInformationByDayFragmentToChooseDayFragment3(
                                        currentWeather
                                    )
                            findNavController().navigate(action)
                        }
                }
            }
        }

        generalDayTodayViewModel.errorMessage.observe(viewLifecycleOwner) { visibility ->
            binding.currentWeatherDescriptionTextview.text = getString(visibility)
        }

        generalDayTodayViewModel.weatherToday.observe(viewLifecycleOwner) {
            initializingScreenForToday(it)
        }

        lifecycleScope.launchWhenStarted {
            generalDayTodayViewModel.progressBar
                .collect { visible ->
                    binding.progressBar.isVisible = visible
                }
        }

        val toolbar = binding.toolbar
        toolbar.setOnMenuItemClickListener {
            switchingAction(it)
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

    private fun switchingAction(it: MenuItem): Boolean {
        when (it.itemId) {
            R.id.action_search -> {
                findNavController().navigate(R.id.action_weatherInformationByDayFragment_to_cityListFragment)
                true
            }
            R.id.switch_mode -> {
                true
            }
            else -> onOptionsItemSelected(it)
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}

