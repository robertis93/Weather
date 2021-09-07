package com.rob.weather.generaldaytoday.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.rob.weather.App
import com.rob.weather.R
import com.rob.weather.databinding.FragmentGeneralDayTodayBinding
import com.rob.weather.generaldaytoday.adapters.GeneralDayTodayAdapter
import com.rob.weather.generaldaytoday.viewmodel.GeneralDayTodayViewModel
import com.rob.weather.model.FullWeatherToday
import com.rob.weather.utils.BaseFragment
import com.rob.weather.utils.Utils.city
import com.squareup.picasso.Picasso
import javax.inject.Inject


class GeneralDayTodayFragment :
    BaseFragment<FragmentGeneralDayTodayBinding>(FragmentGeneralDayTodayBinding::inflate) {
    @Inject
    lateinit var generalDayTodayViewModelFactory: GeneralDayTodayViewModelFactory
    lateinit var generalDayTodayViewModel: GeneralDayTodayViewModel
    lateinit var todayWeather: FullWeatherToday
    private val dialog = ShowDialogForChangingCity()
    lateinit var picasso: Picasso

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        generalDayTodayViewModel = ViewModelProvider(
            this,
            generalDayTodayViewModelFactory
        ).get(GeneralDayTodayViewModel::class.java)

        picasso = Picasso.Builder(requireContext()).build()
        generalDayTodayViewModel.getAllWeatherForecast(city)
        val allDaysWeatherListAdapter = GeneralDayTodayAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.adapter = allDaysWeatherListAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        generalDayTodayViewModel.sortedWeatherForecastResult.observe(viewLifecycleOwner) { list ->
            allDaysWeatherListAdapter.setData(list)
        }

        generalDayTodayViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            binding.currentWeatherDescriptionTextview.text = error
        }

        generalDayTodayViewModel.fullWeatherTodayResponse.observe(viewLifecycleOwner) {
            todayWeather = it
        }

        generalDayTodayViewModel.weatherToday.observe(viewLifecycleOwner) {
            with(binding) {
                currentDateTextView.text = it.date
                currentTemperatureTextview.text = it.temperature
                currentWeatherDescriptionTextview.text = it.description
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
                    dialog.showDialog(requireContext(), generalDayTodayViewModel)
                    true
                }
                R.id.action_loader -> {

                    true
                }
                else -> onOptionsItemSelected(it)
            }
        }

        binding.blueRectangleView.setOnClickListener {
            val action =
                GeneralDayTodayFragmentDirections.actionWeatherInformationByDayFragmentToChooseDayFragment3(
                    todayWeather
                )
            findNavController().navigate(action)
        }

        binding.swipeRefresh.setOnRefreshListener(OnRefreshListener {

            generalDayTodayViewModel.getAllWeatherForecast(city)
            binding.swipeRefresh.isRefreshing = false
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as App).component.inject(this)
    }
}

