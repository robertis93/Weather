package com.rob.weather.generaldaytoday.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
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
import com.rob.weather.utils.BASE_URL_IMAGE
import com.rob.weather.utils.BaseFragment
import com.rob.weather.utils.extensions.getAppComponent
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collect

class GeneralDayTodayFragment :
    BaseFragment<FragmentGeneralDayTodayBinding>(FragmentGeneralDayTodayBinding::inflate) {
    private lateinit var generalDayTodayViewModelFactory: GeneralDayTodayViewModelFactory
    private val generalDayTodayViewModel:
            GeneralDayTodayViewModel by viewModels { generalDayTodayViewModelFactory }

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
            generalDayTodayViewModel.isSunRise.collect {
                binding.backgroundForWeatherIndicatorsView.setBackgroundResource(R.drawable.rectangle_sunrise_)
                binding.intersectView.setBackgroundResource(R.drawable.intersect_sunrise)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            generalDayTodayViewModel.isDay.collect {
                binding.backgroundForWeatherIndicatorsView.setBackgroundResource(R.drawable.rectangle_day)
                binding.intersectView.setBackgroundResource(R.drawable.intersect_day)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            generalDayTodayViewModel.isNight.collect {
                binding.backgroundForWeatherIndicatorsView.setBackgroundResource(R.drawable.rectangle_night)
                binding.intersectView.setBackgroundResource(R.drawable.intersect_night)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            generalDayTodayViewModel.weatherForNextDays.collect { list ->
                allDaysWeatherListAdapter.setData(list)
            }
        }

        binding.backgroundForWeatherIndicatorsView.setOnClickListener {
            generalDayTodayViewModel.getCityFromDB()
        }

        lifecycleScope.launchWhenStarted {
            generalDayTodayViewModel.cityName
                .collect { cityName ->
                    generalDayTodayViewModel.getMoreInformationToday(cityName)
                }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            generalDayTodayViewModel.fullInfoTodayWeather.collect { currentWeather ->
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

        lifecycleScope.launchWhenStarted {
            generalDayTodayViewModel.searchingCity
                .collect {
                    val action =
                        GeneralDayTodayFragmentDirections
                            .actionWeatherInformationByDayFragmentToCityListFragment()
                    findNavController().navigate(action)
                }
        }

        lifecycleScope.launchWhenStarted {
            generalDayTodayViewModel.changingMode
                .collect {
                }
        }

        binding.swipeRefresh.setOnRefreshListener(OnRefreshListener {
            generalDayTodayViewModel.updateWeatherForecastInformation()
        })

        lifecycleScope.launchWhenStarted {
            generalDayTodayViewModel.cityName
                .collect { cityName ->
                    generalDayTodayViewModel.updateInformation(cityName)
                }
        }

        lifecycleScope.launchWhenStarted {
            generalDayTodayViewModel.city
                .collect { cityName ->
                    generalDayTodayViewModel.updateInformation(cityName)
                }
        }

        lifecycleScope.launchWhenStarted {
            generalDayTodayViewModel.updatingInformation
                .collect { visible ->
                    binding.swipeRefresh.isRefreshing = visible
                }
        }
        generalDayTodayViewModel.checkTime()
        generalDayTodayViewModel.checkDataBase()
        generalDayTodayViewModel.checkArguments(arguments)
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
            nameCityToolbar.text = weatherToday.city
            val iconCode = weatherToday.icon
            val iconUrl = BASE_URL_IMAGE + iconCode + ".png"
            binding.weatherIcon.visibility = View.VISIBLE
            Picasso.get().load(iconUrl).into(weatherIcon)
        }
    }


}

