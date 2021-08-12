package com.rob.weather.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rob.weather.R
import com.rob.weather.databinding.FragmentWeatherInformationByDayBinding
import com.rob.weather.view.adapters.AllDaysWeatherListAdapter
import com.rob.weather.view.adapters.WeatherListAdapter

class AllDaysWeatherInformationFragment : BaseFragment<FragmentWeatherInformationByDayBinding>() {

    override fun inflate(inflater: LayoutInflater): FragmentWeatherInformationByDayBinding =
        FragmentWeatherInformationByDayBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val allDaysWeatherListAdapter = AllDaysWeatherListAdapter()
        val recyclerView = binding.recyclerview
        recyclerView.adapter = allDaysWeatherListAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

       // binding.textView.setOnClickListener{Navigation.findNavController(view).navigate(R.id.action_weatherInformationByDayFragment_to_oneDayWeatherInformationFragment)}
    }
}