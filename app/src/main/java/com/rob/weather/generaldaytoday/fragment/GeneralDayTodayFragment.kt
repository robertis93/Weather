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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.rob.weather.R
import com.rob.weather.databinding.FragmentGeneralDayTodayBinding
import com.rob.weather.generaldaytoday.adapters.GeneralDayTodayAdapter
import com.rob.weather.generaldaytoday.viewmodel.GeneralDayTodayViewModel
import com.rob.weather.utils.BaseFragment
import com.rob.weather.utils.Utils.BASE_URL_IMAGE
import com.rob.weather.utils.Utils.city
import com.rob.weather.utils.extensions.getAppComponent
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
        generalDayTodayViewModel.getAllWeatherForecast(city)
        val allDaysWeatherListAdapter = GeneralDayTodayAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.adapter = allDaysWeatherListAdapter
        generalDayTodayViewModel.sortedWeatherForecastResult.observe(viewLifecycleOwner) { list ->
            allDaysWeatherListAdapter.setData(list)
        }

        generalDayTodayViewModel.firstSortedWeatherForecastResult.observe(viewLifecycleOwner) {
            val action =
                GeneralDayTodayFragmentDirections
                    .actionWeatherInformationByDayFragmentToChooseDayFragment3(
                        it[0]
                    )
            binding.blueRectangleView.setOnClickListener {
                findNavController().navigate(action)
            }
        }

        generalDayTodayViewModel.progressBar.observe(viewLifecycleOwner){ visibility ->
            binding.progressBar.isVisible = visibility
        }

        generalDayTodayViewModel.errorMessage.observe(viewLifecycleOwner){ visibility ->
            binding.currentWeatherDescriptionTextview.text = getString(visibility)
        }

        generalDayTodayViewModel.weatherToday.observe(viewLifecycleOwner) {
            with(binding) {
                currentDateTextView.text = requireContext().getString(R.string.today_with_comma) + it.date
                currentTemperatureTextview.text = it.temperature +
                        requireContext().getString(R.string.celsius_icon)
                currentWeatherDescriptionTextview.text =
                    it.description + requireContext().getString(R.string.feels_like) + it.temperature + requireContext().getString(R.string.celsius_icon)
                toolbarToday.text = it.city
                val iconCode = it.icon
                val iconUrl = BASE_URL_IMAGE + iconCode + ".png"
                binding.weatherIcon.visibility = View.VISIBLE
                Picasso.get().load(iconUrl).into(weatherIcon)
            }
        }

        val toolbar = binding.toolbar
        toolbar.setOnMenuItemClickListener {
            switchingAction(it)
        }
        binding.swipeRefresh.setOnRefreshListener(OnRefreshListener {
            generalDayTodayViewModel.getAllWeatherForecast(city)
            binding.swipeRefresh.isRefreshing = false
        })
    }



    private fun switchingAction(it: MenuItem) : Boolean{
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

