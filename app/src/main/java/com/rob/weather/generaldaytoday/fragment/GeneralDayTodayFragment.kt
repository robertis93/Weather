package com.rob.weather.generaldaytoday.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.rob.weather.App
import com.rob.weather.R
import com.rob.weather.databinding.FragmentGeneralDayTodayBinding
import com.rob.weather.generaldaytoday.adapters.GeneralDayTodayAdapter
import com.rob.weather.generaldaytoday.viewmodel.GeneralDayTodayViewModel
import com.rob.weather.model.SortedByDateWeatherForecastResult
import com.rob.weather.utils.BaseFragment
import com.rob.weather.utils.Utils.BASE_URL_IMAGE
import com.rob.weather.utils.Utils.city
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class GeneralDayTodayFragment :
    BaseFragment<FragmentGeneralDayTodayBinding>(FragmentGeneralDayTodayBinding::inflate) {
    @Inject
    lateinit var generalDayTodayViewModelFactory: GeneralDayTodayViewModelFactory
    val generalDayTodayViewModel: GeneralDayTodayViewModel by viewModels { generalDayTodayViewModelFactory }
    lateinit var todayWeather: List<SortedByDateWeatherForecastResult>
    lateinit var picasso: Picasso

    override fun onAttach(context: Context) {
        super.onAttach(context)
        generalDayTodayViewModelFactory = (activity?.application as App).component
            .getDependencyGeneralDay()
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        picasso = Picasso.Builder(requireContext()).build()
        generalDayTodayViewModel.getAllWeatherForecast(requireContext(), city)
        val allDaysWeatherListAdapter = GeneralDayTodayAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.adapter = allDaysWeatherListAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        generalDayTodayViewModel.sortedWeatherForecastResult.observe(viewLifecycleOwner) { list ->
            allDaysWeatherListAdapter.setData(list)
        }

        generalDayTodayViewModel.firstSortedWeatherForecastResult.observe(viewLifecycleOwner){
            todayWeather = it
        }

        generalDayTodayViewModel.errorMessage
            .onEach { error ->
                binding.currentWeatherDescriptionTextview.text = error
                binding.progressBar.visibility = View.GONE
            }
            .launchIn(lifecycleScope)

//        generalDayTodayViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
//            binding.currentWeatherDescriptionTextview.text = error
//            binding.progressBar.visibility = View.GONE
//        }

        generalDayTodayViewModel.weatherToday.observe(viewLifecycleOwner) {
            with(binding) {
                currentDateTextView.text = it.date
                currentTemperatureTextview.text = it.temperature
                currentWeatherDescriptionTextview.text = it.description
                toolbarToday.text = it.city
                val iconCode = it.icon
                val iconUrl = BASE_URL_IMAGE + iconCode + getString(R.string.png)
                binding.weatherIcon.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                Picasso.get().load(iconUrl).into(weatherIcon)
            }
        }

        val toolbar = binding.toolbar
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_search -> {
                    findNavController().navigate(R.id.action_weatherInformationByDayFragment_to_cityListFragment)
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
                GeneralDayTodayFragmentDirections
                    .actionWeatherInformationByDayFragmentToChooseDayFragment3(
                    todayWeather.get(0)
                )
            findNavController().navigate(action)
        }

        binding.swipeRefresh.setOnRefreshListener(OnRefreshListener {
            generalDayTodayViewModel.getAllWeatherForecast(requireContext(), city)
            binding.swipeRefresh.isRefreshing = false
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}

