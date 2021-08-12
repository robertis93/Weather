package com.rob.weather.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.rob.weather.databinding.FragmentOneDayWeatherInformationBinding
import com.rob.weather.view.adapters.WeatherListAdapter

class OneDayWeatherInformationFragment : BaseFragment<FragmentOneDayWeatherInformationBinding>() {
    override fun inflate(inflater: LayoutInflater): FragmentOneDayWeatherInformationBinding =
        FragmentOneDayWeatherInformationBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weatherListAdapter = WeatherListAdapter()
        val recyclerView = binding.weatherRecyclerView
        recyclerView.adapter = weatherListAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}
