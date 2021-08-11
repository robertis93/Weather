package com.rob.weather.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.rob.weather.R
import com.rob.weather.databinding.FragmentWeatherInformationByDayBinding

class AllDaysWeatherInformationFragment : BaseFragment<FragmentWeatherInformationByDayBinding>() {

    override fun inflate(inflater: LayoutInflater): FragmentWeatherInformationByDayBinding =
        FragmentWeatherInformationByDayBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textView.setOnClickListener{Navigation.findNavController(view).navigate(R.id.action_weatherInformationByDayFragment_to_oneDayWeatherInformationFragment)}
    }
}