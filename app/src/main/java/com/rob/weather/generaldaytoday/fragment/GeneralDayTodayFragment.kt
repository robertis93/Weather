package com.rob.weather.generaldaytoday.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rob.weather.R
import com.rob.weather.databinding.FragmentGeneralDayTodayBinding
import com.rob.weather.generaldaytoday.adapters.GeneralDayTodayAdapter
import com.rob.weather.generaldaytoday.repository.WeatherForecastRepository
import com.rob.weather.generaldaytoday.retrofit.RemoteDataSource
import com.rob.weather.generaldaytoday.viewmodel.GeneralDayTodayViewModel
import com.rob.weather.model.FullWeatherToday
import com.rob.weather.model.WeatherForecastResult
import com.rob.weather.utils.BaseFragment
import com.rob.weather.utils.Utils.city
import com.rob.weather.utils.Utils.id_key
import com.squareup.picasso.Picasso
import javax.inject.Inject

class GeneralDayTodayFragment :
    BaseFragment<FragmentGeneralDayTodayBinding>(FragmentGeneralDayTodayBinding::inflate) {

   lateinit var todayWeather : FullWeatherToday
    @Inject
    lateinit var viewModel: GeneralDayTodayViewModel

    private val dialog = ShowDialogForChangingCity()
    lateinit var picasso : Picasso

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (context)
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, GeneralDayTodayViewModelFactory(WeatherForecastRepository(id_key,
            RemoteDataSource.RetrofitServices.getClient("https://api.openweathermap.org/")))
        ).get(GeneralDayTodayViewModel::class.java)

        picasso = Picasso.Builder(requireContext()).build()

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
            binding.currentWeatherDescriptionTextview.text = error
        }

        viewModel.fullWeatherTodayResponse.observe(viewLifecycleOwner){
            todayWeather = it
        }

        viewModel.weatherToday.observe(viewLifecycleOwner) {
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
            val action = GeneralDayTodayFragmentDirections.actionWeatherInformationByDayFragmentToChooseDayFragment3(todayWeather)
            findNavController().navigate(action)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}

